package com.cmft.slas.cmuop.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.cmft.slas.cmuop.common.constant.*;
import com.cmft.slas.cmuop.mapper.*;
import com.cmft.slas.cmuop.service.EntityAppService;
import com.cmft.slas.common.utils.JsonUtil;
import com.yuancore.radosgw.sdk.common.util.json.JSONUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.cmft.slas.cmuop.common.dto.CmuopPageInfo;
import com.cmft.slas.cmuop.common.service.RedisLockService;
import com.cmft.slas.cmuop.dto.ArticleForSortDTO;
import com.cmft.slas.cmuop.dto.RelatedEntityDTO;
import com.cmft.slas.cmuop.dto.UserEntityDTO;
import com.cmft.slas.cmuop.entity.LastPosition;
import com.cmft.slas.cmuop.entity.UserEntity;
import com.cmft.slas.cmuop.processor.ArticleFilterProcessor;
import com.cmft.slas.cmuop.processor.ArticlePreviewProcessor;
import com.cmft.slas.cmuop.processor.processorImpl.ArticleSortHandleProcessor;
import com.cmft.slas.cmuop.processor.processorImpl.ArticleSortPrepareProcessor;
import com.cmft.slas.cmuop.processor.processorImpl.IfReadProcessor;
import com.cmft.slas.cmuop.service.ArticleListSortService;
import com.cmft.slas.cmuop.service.CmuopEntityService;
import com.cmft.slas.cmuop.vo.AppArticlePreview;
import com.cmft.slas.common.pojo.PageInfo;
import com.cmft.slas.common.utils.BeanMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

@Slf4j
@Service
@EnableAsync
public class ArticleListSortServiceImpl implements ArticleListSortService {

    @Value("${cmuop.mNumber}")
    private String mNumber;

    @Value("${cmuop.nNumber}")
    private String nNumber;

    @Value("${cmuop.userNum}")
    private String userNum;

    @Value("${cmuop.expireTime}")
    private String expireTimeStr;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private UserEntityMapper userEntityMapper;

    @Autowired
    private ArticleRelatedEntityMapper articleRelatedEntityMapper;

    @Autowired
    private CmuopEntityService cmuopEntityService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ArticleSortPrepareProcessor articleSortPrepareProcessor;

    @Autowired
    private ArticleSortHandleProcessor articleSortHandleProcessor;

    @Autowired
    private ArticlePreviewProcessor articlePreviewProcessor;

    @Autowired
    private ArticleFilterProcessor articleFilterProcessor;

    @Autowired
    private UserNotLikeMapper userNotLikeMapper;

    @Autowired
    private RedisLockService redisLockService;

    @Autowired
    private IfReadProcessor ifReadProcessor;

    @Autowired
    private EntityAppService entityAppService;

    @Autowired AsyncHelper asyncHelper;
    @Override
    public PageInfo<AppArticlePreview> getSortedArticleList(String uid, String entityType, String sentimentType,
        String columnType, String lastPosition, Integer pageSize, Boolean ifFirstTime) {
        // 文章list
        List<ArticleForSortDTO> articleList = Lists.newArrayList();
        // 返回文章的list
        List<AppArticlePreview> res = Lists.newArrayList();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int i = 0;
        if (StringUtils.isBlank(columnType) && StringUtils.isBlank(sentimentType)) {
            while (CollectionUtils.isEmpty(res)) {
                articleList = getSortedArticles(uid, entityType, lastPosition, pageSize, ifFirstTime);
                if (CollectionUtils.isEmpty(articleList)) {
                    break;
                }
                lastPosition = String.valueOf(articleList.get(articleList.size() - 1).getTArticleId());
                res = transfer(articleList, uid);
            }

            articlePreviewProcessor.processPreview(res);
            ifReadProcessor.processIfRead(res, uid);
            // 返回结果
            return new CmuopPageInfo<>(res)
                .setLastPosition(res.size() > 0 ? String.valueOf(res.get(res.size() - 1).getTArticleId()) : null);
        } else if(ColumnTypes.LEADER.getValue().equals(columnType) && IfEntrepreneurSorted(entityType)){
            articleList = getLeaderList(entityType, lastPosition, sentimentType, pageSize, uid);
            if(CollectionUtils.isNotEmpty(articleList)){
                for(ArticleForSortDTO article : articleList){
                    AppArticlePreview aap = new AppArticlePreview();
                    BeanUtils.copyProperties(article, aap);
                    res.add(aap);
                }
                ArticleForSortDTO lastArticle = articleList.get(res.size()-1);
                LastPosition lp = new LastPosition()
                        .setOrderNum(lastArticle.getOrderNum())
                        .setPubTime(formatter.format(lastArticle.getPubTime()));
                lastPosition = JsonUtil.toJson(lp);
                res = articleFilterProcessor.process(res, uid, false);
                articlePreviewProcessor.processPreview(res);
                ifReadProcessor.processIfRead(res, uid);
            }
            return new CmuopPageInfo<>(res).setLastPosition(lastPosition);
        } else {
            // 有三级分类要求时无需排序
            while (CollectionUtils.isEmpty(res)) {
                articleList = getColumnArticleList(entityType, columnType, lastPosition,
                        StringUtils.isBlank(sentimentType) ? null : Integer.valueOf(sentimentType), pageSize, uid);
                if (CollectionUtils.isEmpty(articleList)) {
                    break;
                }
                lastPosition = formatter.format(articleList.get(articleList.size() - 1).getPubTime());
                for (; i < Math.min(articleList.size(), pageSize); i++) {
                    ArticleForSortDTO article = articleList.get(i);
                    AppArticlePreview aap = new AppArticlePreview();
                    BeanUtils.copyProperties(article, aap);
                    res.add(aap);
                }
                // oa block filter
                res = articleFilterProcessor.process(res, uid, false);
            }

            // 返回结果

            articlePreviewProcessor.processPreview(res);
            ifReadProcessor.processIfRead(res, uid);
            return new CmuopPageInfo<>(res).setLastPosition(lastPosition);
        }

    }

    private List<AppArticlePreview> transfer(List<ArticleForSortDTO> articleList, String uid) {
        int i = 0;
        List<AppArticlePreview> res = Lists.newArrayList();
        for (; i < articleList.size(); i++) {
            ArticleForSortDTO article = articleList.get(i);
            AppArticlePreview aap = new AppArticlePreview();
            BeanUtils.copyProperties(article, aap);
            res.add(aap);
        }
        // res = articleFilterProcessor.process(res, uid);
        return res;
    }

    private boolean IfEntrepreneurSorted(String entityCode){
        if(StringUtils.isBlank(entityCode))
            entityCode = Entity.CMG.getCode();
        Example example = new Example(UserEntity.class);
        example.createCriteria()
                .andEqualTo("entityCode", entityCode)
                .andEqualTo("isDelete", (byte)0)
                .andNotEqualTo("orderNum", LeaderOrder.DEFAULT.getValue());
        return userEntityMapper.selectCountByExample(example) > 0;
    }

    private List<ArticleForSortDTO> getLeaderList(String entityType, String lastPosition, String sentiment, Integer pageSize, String uid){
        List<ArticleForSortDTO> res = Lists.newArrayList();
        List<String> userNotLikeList = userNotLikeMapper.selectUserNotLike(uid);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(StringUtils.isBlank(entityType))
            entityType = Entity.CMG.getCode();
        String lastTime;
        Integer orderNum;
        if(StringUtils.isNotBlank(lastPosition)){
            LastPosition lp = JsonUtil.fromJson(lastPosition, LastPosition.class);
            lastTime = lp.getPubTime();
            orderNum = lp.getOrderNum();
        }else{
            res.addAll(articleMapper.selectColumnStickArticle(ColumnTypes.LEADER.getValue(),
                    entityType,
                    StringUtils.isBlank(sentiment) ? null : Integer.parseInt(sentiment),
                    userNotLikeList));
            lastTime = formatter.format(new Date());
            orderNum = 0;
        }
        res.addAll(articleMapper.selectLeaderArticleWithCursor(entityType,
                lastTime,
                orderNum,
                StringUtils.isBlank(sentiment) ? null : Integer.parseInt(sentiment),
                pageSize,
                userNotLikeList));
        return res;
    }

    private List<ArticleForSortDTO> getColumnArticleList(String entityType, String columnType, String lastPosition,
        Integer sentimentType, Integer pageSize, String uid) {
        List<String> userNotLikeList = userNotLikeMapper.selectUserNotLike(uid);
        if (ColumnTypes.POINTOFVIEW.getValue().equals(columnType)) {
            if (StringUtils.isBlank(lastPosition)) {
                List<ArticleForSortDTO> list =
                    articleMapper.selectColumnStickArticle(columnType, null, sentimentType, userNotLikeList);
                list.addAll(articleMapper.selectOrderArticle(columnType, null, sentimentType, null, userNotLikeList));
                list.addAll(articleMapper.selectColumnArticle(columnType, null, sentimentType, null, pageSize,
                    userNotLikeList));
                return list;
            }
            return articleMapper.selectColumnArticle(columnType, null, sentimentType, lastPosition, pageSize,
                userNotLikeList);
        }
        if (StringUtils.isBlank(entityType)) {
            entityType = Constant.ZHAOWEN;
        }
        if (StringUtils.isBlank(lastPosition)) {
            List<ArticleForSortDTO> list =
                articleMapper.selectColumnStickArticle(columnType, entityType, sentimentType, userNotLikeList);
            list.addAll(articleMapper.selectOrderArticle(columnType, entityType, sentimentType, null, userNotLikeList));
            list.addAll(articleMapper.selectColumnArticle(columnType, entityType, sentimentType, null, pageSize,
                userNotLikeList));
            return list;
        }
        return articleMapper.selectColumnArticle(columnType, entityType, sentimentType, lastPosition, pageSize,
            userNotLikeList);
    }

    private List<ArticleForSortDTO> getSortedArticles(String uid, String entityType, String lastPosition,
        Integer pageSize, Boolean firstTime) {
        // 候选文章list
        List<ArticleForSortDTO> articleList = Lists.newArrayList();
        // 置顶文字list
        List<ArticleForSortDTO> stickArticles = Lists.newArrayList();
        // 返回文章list
        List<ArticleForSortDTO> res = Lists.newArrayList();

        /*
         * 临时处理卡片 只取三篇文章
         */
        if (StringUtils.isBlank(lastPosition) && SortConstants.CARD_SIZE.getValue().equals(pageSize)) {
            stickArticles = getStickAndMuteArticles(entityType);
            res.addAll(stickArticles.stream().limit(3).collect(Collectors.toList()));
            log.info("camouflage started at {} for {}", entityType, uid);
            loadingCamouflage(res, uid);
            log.info("camouflage ends~ at {} for {}", entityType, uid);
            return res;
        }


        if (StringUtils.isBlank(lastPosition)) {
            stickArticles = getStickAndMuteArticles(entityType);
            articleList = loadingCache(stickArticles, entityType, uid, firstTime);
            for (int i = 0; i < Math.min(articleList.size() - 1, pageSize); i++) {
                res.add(articleList.get(i));
            }
            return res;
        } else {
            int i = 0;
            boolean flag = false;
            articleList = getList(uid, SortConstants.NORMAL.getValue(), entityType, firstTime);
            articleSortHandleProcessor.process(articleList, uid);
            for (; i < articleList.size(); i++) {
                if (articleList.get(i) != null && articleList.get(i).getTArticleId() != null
                    && articleList.get(i).getTArticleId().equals(Long.valueOf(lastPosition))) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                i += 1;
            } else {
                i = 0;
            }
            if (i >= articleList.size()) {
                articleList = getList(uid, SortConstants.RUNOUT.getValue(), entityType, firstTime);
                articleList = articleFilterProcessor.process(uid, articleList, true);
                articleSortHandleProcessor.process(articleList, uid);
                i = 0;
            }

            for (int j = i; j < Math.min(articleList.size(), i + pageSize); j++) {
                res.add(articleList.get(j));
            }
        }

        // articleSortPrepareProcessor.process(res, uid);

        return res;
    }

    private List<ArticleForSortDTO> getStickAndMuteArticles(String entityType){
        // 取出置顶
        List<ArticleForSortDTO> stickArticles = articleMapper.selectStickArticleList(StringUtils.isBlank(entityType) ? FakeEntity.ZHAOWEN.getCode() : entityType);

        // @liurp001 get fake sticky items
        List<ArticleForSortDTO> muteSticks = articleMapper.selectMuteSticks(
                StringUtils.isBlank(entityType) ? FakeEntity.ZHAOWEN.getCode() : entityType);
        stickArticles.addAll(muteSticks);
        return stickArticles;
    }

    private void loadingCamouflage(List<ArticleForSortDTO> cardArticles, String uid){
        log.info("start");
        asyncHelper.loadingCacheAsync("", uid);
        List<String> entities = entityAppService.getAllUserEntityCode(uid);
        List<String> cardEntities = getCardEntities(cardArticles);
        if(CollectionUtils.isNotEmpty(cardEntities)){
            entities.addAll(cardEntities);
            entities = entities.stream().distinct().collect(Collectors.toList());
        }
        log.info("total entity size: {}", entities.size());
        entities.forEach(entity -> {
            log.info("Now let's witness the magic ~~~~ at {} for {}", entity, uid);
            asyncHelper.loadingCacheAsync( entity, uid);
            log.info("Tricks at {} for {} is done, and no one is the wiser >///<", entity, uid);
        });
    }

    private List<String> getCardEntities(List<ArticleForSortDTO> cardArticles){
        if(CollectionUtils.isNotEmpty(cardArticles)){
            List<AppArticlePreview> previews = transfer(cardArticles, null);
            articlePreviewProcessor.processPreview(previews);
            return previews.stream().map(AppArticlePreview::getEntityCode).collect(Collectors.toList());
        }
        return null;
    }

    @Component
    class AsyncHelper{
        @Async
        void loadingCacheAsync(String entityType, String uid) {
            // 如果不是从redis池取出，则重新生成添加到redis
            Boolean isExists = checkArticleListFromRedisWithUid(uid, entityType);
            if(!isExists) {
                List<ArticleForSortDTO>  articleList = Lists.newArrayList();
                // 获取实体-推荐文章
                articleList = getList(uid, SortConstants.REFRESH.getValue(), entityType, true);
                setArticleListFromRedisWithUid(uid, articleList, entityType);
            }
        }
    }

    private List<ArticleForSortDTO> loadingCache(List<ArticleForSortDTO> stickArticles ,String entityType, String uid, Boolean firstTime){
        List<ArticleForSortDTO> articleList =  Lists.newArrayList();
        // 获取实体-推荐文章
        articleList = getList(uid, SortConstants.REFRESH.getValue(), entityType, firstTime);
        articleSortPrepareProcessor.process(stickArticles);
        // TODO add stick article to redis

        // articleSortPrepareProcessor.process(articleList, uid);
        articleSortHandleProcessor.process(articleList, uid);
        articleList = combineList(stickArticles, articleList);

        // block filter
        articleList = articleFilterProcessor.process(uid, articleList, true);

        // 添加到redis
        setArticleListFromRedisWithUid(uid, articleList, entityType);
        return articleList;
    }

    /**
     * 从redis获取用户的相应实体-推荐池子
     * @param uid
     * @param entityCode
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<ArticleForSortDTO> getArticleListFromRedisWithUid(String uid, String entityCode) {
        return (List<ArticleForSortDTO>)redisTemplate.opsForValue().get(getArticleListByUidRedisKey(uid, entityCode));
    }

    /**
     * 从redis获取用户的相应实体-推荐池子是否存在
     * @param uid
     * @param entityCode
     * @return
     */
    @SuppressWarnings("unchecked")
    private Boolean checkArticleListFromRedisWithUid(String uid, String entityCode) {
        String key = getArticleListByUidRedisKey(uid, entityCode);
        return redisTemplate.hasKey(key) && redisTemplate.opsForValue().size(key) > 0 ;
    }

    private void setArticleListFromRedisWithUid(String uid, List<ArticleForSortDTO> list, String entityCode) {
        Long timeout = Long.valueOf(expireTimeStr);
        redisTemplate.opsForValue().set(getArticleListByUidRedisKey(uid, entityCode), list, timeout, TimeUnit.SECONDS);
    }

    private List<ArticleForSortDTO> getArticleListFromMysql(String entityType, String columnType, Long offset,
        Integer m) {
        // 获取redis key
        String key = null;
        if (StringUtils.isNotBlank(entityType) && StringUtils.isBlank(columnType)) {
            key = getEntityKey(entityType);
        } else if (StringUtils.isNotBlank(columnType) && StringUtils.isBlank(entityType)) {
            key = getColumnKey(columnType);
        } else {
            key = getColumnKey(columnType);
        }

        /*
         * 判断上一个执行者是否把文章补全了
         */
        Set<Object> articleSet = Sets.newHashSet();
        if (offset == 0l) {
            articleSet = redisTemplate.opsForZSet().range(getEntityKey(entityType), 0, m);
        } else {
            articleSet = redisTemplate.opsForZSet().rangeByScore(getEntityKey(entityType), offset, 0, 0, m);
        }
        // 上一个执行者已经把文章补全了
        if (CollectionUtils.isNotEmpty(articleSet)) {
            List<ArticleForSortDTO> articleList = articleSet.stream().map(o -> {
                return BeanMapper.map(o, ArticleForSortDTO.class);
            }).collect(Collectors.toList());
            return articleList;
        }

        /*
         * 若沒有補到文章，則自己再請求一次
         */
        Long lastPositionOffsetOfPubTime = null;

        if (offset != null && offset != 0l) {
            lastPositionOffsetOfPubTime = -offset;
        }

        // 倒序取出 第一个时间最大、最新
        List<ArticleForSortDTO> articleList =
                articleMapper.selectArticleListByType(columnType, entityType, lastPositionOffsetOfPubTime, m);

        // 按倒序放入redis 并生成单调递增的score
        if (CollectionUtils.isNotEmpty(articleList)) {
            articleSortPrepareProcessor.process(articleList);
            for (int i = 1; i <= articleList.size(); i++) {
                redisTemplate.opsForZSet().add(key, articleList.get(i - 1),
                        -articleList.get(i - 1).getPubTime().getTime());
            }
        }

        return new ArrayList<ArticleForSortDTO>(articleList);

    }

    private List<ArticleForSortDTO> getArticleListFromEntityRedis(String entityType, LastPosition lp, Integer size) {
        Long m = (long)size;

        // 设定的实体池文章数量大小
        int setEntityQueueSize = Integer.valueOf(mNumber);

        // 从实体池子返回的文章
        List<ArticleForSortDTO> res = Lists.newArrayList();

        // 获取offset
        Long offset = 0L;
        if (lp.getOffset() != null) {
            offset = (long)lp.getOffset();
        }

        Set<Object> articleSet = Sets.newHashSet();
        if (offset == 0l) {
            articleSet = redisTemplate.opsForZSet().range(getEntityKey(entityType), 0, m);
        } else {
            articleSet = redisTemplate.opsForZSet().rangeByScore(getEntityKey(entityType), offset, 0, 0, m);
            // log.info("本次从实体{}的redis获取文章{}篇", entityType,
            // CollectionUtils.isNotEmpty(articleSet) ? articleSet.size() : 0);
        }

        res = articleSet.stream().map(o -> {
            return BeanMapper.map(o, ArticleForSortDTO.class);
        }).collect(Collectors.toList());

        // 返回给用户队列的文章数量
        int nowSize = res.size();
        // 目前实际的实体池文章数量大小
        long nowentityQueueSize = redisTemplate.opsForZSet().size(getEntityKey(entityType));
        if ((CollectionUtils.isEmpty(res) || nowSize < size) && nowentityQueueSize <= setEntityQueueSize) {
            // 开始补数的offset，后面转换成pubTime
            offset = CollectionUtils.isEmpty(res) ? offset : -res.get(nowSize - 1).getPubTime().getTime();
            // 实体文章池子要补的大小
            Integer entityQueueNeedSize = (int)(setEntityQueueSize - nowentityQueueSize);
            List<ArticleForSortDTO> dbReturnArtileList =
                getArticleListFromMysql(entityType, null, offset, entityQueueNeedSize);

            // 当前返回文章要补的大小
            int needSize = size - nowSize;
            List<ArticleForSortDTO> needArtileList =
                CollectionUtils.isNotEmpty(dbReturnArtileList) && dbReturnArtileList.size() > needSize
                    ? dbReturnArtileList.subList(0, needSize) : dbReturnArtileList;
            res.addAll(needArtileList);
        }

        if (res.size() < size) {
            lp.setRunOut(true);
        }

        // 设置新的offset 不超过设定的池子大小
        if (CollectionUtils.isNotEmpty(res)) {
            long nowOffset = -res.get(res.size() - 1).getPubTime().getTime();
            lp.setOffset(nowOffset);
        } else {
            lp.setOffset(null);
        }

        return res;
    }

    private String getArticleListByUidRedisKey(String uid, String entityCode) {
        return String.format("slas:cmuop:article:user:%s:entity:%s", uid, entityCode);
    }

    private String getLastPositionUidRedisKey(String uid) {
        return String.format("slas:cmuop:article:lastposition:%s", uid);
    }

    private String getEntityKey(String entityCode) {
        return String.format("slas:cmuop:article:entity:%s", entityCode);
    }

    private String getColumnKey(String columnType) {
        return String.format("slas:cmuop:article:column:%s", columnType);
    }

    private List<ArticleForSortDTO> combineList(List<ArticleForSortDTO> list1, List<ArticleForSortDTO> list2) {
        list1.addAll(list2);
        Set<String> articleIdSet = new HashSet<>();
        List<ArticleForSortDTO> res = Lists.newArrayList();
        for (ArticleForSortDTO article : list1) {
            if (articleIdSet.contains(article.getArticleId())) {
                continue;
            }
            articleIdSet.add(article.getArticleId());
            res.add(article);
        }
        return res;
    }

    /**
     * 获取全部-推荐文章
     * 
     * @param entityType
     * @param uid
     * @param status
     * @return
     */
    private List<ArticleForSortDTO> getList(String uid, Integer status, String entityType, Boolean firstTime) {
        List<ArticleForSortDTO> list = getArticleListFromRedisWithUid(uid, entityType);

        Map<String, LastPosition> map = getLastPosition(uid);

        Integer usernum = Integer.valueOf(userNum);

        switch(SortConstants.getByValue(status)){
            case RUNOUT:
                list = null;
                break;
            case REFRESH:
                // 存在且第一次进入 直接返回list
                if(CollectionUtils.isNotEmpty(list) && firstTime){
                    return list;
                }

                list = null;
                map = new TreeMap<>();
                break;
            default:
                break;
        }

        if (CollectionUtils.isEmpty(list)) {
            // 取出用户对应实体序列 4、3、2、1、0
            Map<String, LinkedHashMap<String, RelatedEntityDTO>> levelMap = new TreeMap<>();
            if(StringUtils.isNotBlank(entityType)) {
                levelMap = getLevelMap(uid, entityType);
            } else {
                levelMap = getLevelMap(uid);
            }

            list = Lists.newArrayList();
            boolean breakFlag = false;
            // 各等级实体（按比例）共需要取出的文章数量
            Integer levelNeedNum = 0;
            // keySet 4、3、2、1、0排序
            Set<String> keySet = levelMap.keySet();
            keySet.stream().sorted(Comparator.reverseOrder());
            log.info("获取全部-推荐文章的实体相关等级排序从大到小为：{}", keySet.toString());

            for (String key : keySet) {
                if (breakFlag) {
                    break;
                }

                if ("4".equals(key)) {
                    // 直接对应实体比重为0.4
                    levelNeedNum = (int)((usernum - list.size()) / 2.5);
                } else if ("3".equals(key)) {
                    // 上下级公司对应实体比重为剩下的0.6
                    levelNeedNum = (int)((usernum - list.size()) / 1.6);
                } else if ("2".equals(key)) {
                    // 兜底，招商局集团相关实体比重为剩下的0.6
                    int needNum = usernum - list.size();
                    levelNeedNum = (int)(needNum / 1.6);
                } else if ("1".equals(key)) {
                    // 兜底 其他关联实体实体补全
                    int needNum = usernum - list.size();
                    levelNeedNum = needNum;
                }

                LinkedHashMap<String, RelatedEntityDTO> relatedEntityMap = levelMap.get(key);
                Set<Entry<String, RelatedEntityDTO>> relatedEntity = relatedEntityMap.entrySet();
                if (!CollectionUtils.isEmpty(relatedEntity)) {
                    int addNum = 0;
                    int loopTime = 0;
                    for (Entry<String, RelatedEntityDTO> entry : relatedEntity) {
                        String entityCode = entry.getKey();
                        LastPosition lp = map.get(entityCode);
                        if (lp == null) {
                            lp = new LastPosition();
                            lp.setEntityCode(entityCode);
                            lp.setRunOut(false);
                        } else {
                            if (lp.getRunOut()) {
                                continue;
                            }
                        }

                        // 保证分给此level的数量 在 本level内优先补齐
                        int needNum = (levelNeedNum - addNum) / (relatedEntity.size() - loopTime);

                        List<ArticleForSortDTO> tempList = getArticleListFromEntityRedis(entityCode, lp, needNum);
                        map.put(entityCode, lp);
                        for (ArticleForSortDTO article : tempList) {
                            article.setEntityStep(Integer.valueOf(key));
                            article.setEntityMain(Integer.valueOf(key) > 1);
                        }
                        // 获取combine前的大小
                        int beforeAddNum = list.size();
                        list = combineList(list, tempList);
                        // 获取combine前的大小
                        int afterAddNum = list.size();
                        // 计算此次combine实体新增的文章数量
                        addNum = afterAddNum - beforeAddNum > 0 ? afterAddNum - beforeAddNum : 0;
//                        log.info("用户{}本次需level为{}的实体池中返回文章{}篇，还差{}篇，本次计算从实体{}的redis池需要请求补充文章{}篇，实际补充{}篇", uid, key,
//                            levelNeedNum, levelNeedNum - addNum, entityCode, needNum, addNum);

                        loopTime++;
                        if (list.size() >= usernum) {
                            breakFlag = true;
                            break;
                        }
                    }
                }
            }
            // save lp map
            saveLastPosition(uid, map);
            // 补全
            articleSortPrepareProcessor.process(list, uid);
            // 排序
            // articleSortHandleProcessor.process(list);

            // 存入缓存
            setArticleListFromRedisWithUid(uid, list, entityType);
        }

        return list;
    }

    /**
     * 获取首页-全部的levelMap
     * 
     * @param uid
     * @return
     */
    private Map<String, LinkedHashMap<String, RelatedEntityDTO>> getLevelMap(String uid) {
        LinkedHashMap<String, RelatedEntityDTO> relatedEntityMap = cmuopEntityService.getRelatedEntityMap(false);
        Map<String, LinkedHashMap<String, RelatedEntityDTO>> levelMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }

        });

        // 四级
        LinkedHashMap<String, RelatedEntityDTO> map4 = new LinkedHashMap<>();
        List<String> userEntityList = getEntityListByUid(uid);
        for (String entityCode : userEntityList) {
            map4.put(entityCode, new RelatedEntityDTO());
        }
        levelMap.put("4", map4);

        // 三级
        LinkedHashMap<String, RelatedEntityDTO> map3 = new LinkedHashMap<>();
        for (String entityCode : userEntityList) {
            if (relatedEntityMap.get(entityCode) != null) {
                // 父级
                map3.put(relatedEntityMap.get(entityCode).getParentCode(), new RelatedEntityDTO());
                // 子级
                List<String> childrenLis = relatedEntityMap.get(entityCode).getChildrenList();
                for (String children : childrenLis) {
                    if (relatedEntityMap.get(entityCode) != null) {
                        map3.put(children, new RelatedEntityDTO());
                        relatedEntityMap.remove(children);
                    }
                }

                // 清除已经添加的
                relatedEntityMap.remove(relatedEntityMap.get(entityCode).getParentCode());
                relatedEntityMap.remove(entityCode);
            }
        }
        levelMap.put("3", map3);

        // 二级
        LinkedHashMap<String, RelatedEntityDTO> map2 = new LinkedHashMap<>();
        map2.put("001", new RelatedEntityDTO());
        if (relatedEntityMap.get("001") != null) {
            relatedEntityMap.remove("001");
        }
        levelMap.put("2", map2);

        // 一级
        levelMap.put("1", relatedEntityMap);

        log.info("返回的首页全部实体相关levelMap为:{}", JSON.toJSONString(levelMap));

        return levelMap;
    }

    /**
     * 获取实体-推荐的levelMap
     * 
     * @param uid
     * @param entityCode
     * @return
     */
    private Map<String, LinkedHashMap<String, RelatedEntityDTO>> getLevelMap(String uid, String entityCode) {
        LinkedHashMap<String, RelatedEntityDTO> relatedEntityMap = cmuopEntityService.getRelatedEntityMap(false);
        Map<String, LinkedHashMap<String, RelatedEntityDTO>> levelMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }

        });

        // 四级
        LinkedHashMap<String, RelatedEntityDTO> map4 = new LinkedHashMap<>();
        map4.put(entityCode, new RelatedEntityDTO());
        levelMap.put("4", map4);

        // 三级
        LinkedHashMap<String, RelatedEntityDTO> map3 = new LinkedHashMap<>();
        if (relatedEntityMap.get(entityCode) != null) {
            // 父级
            map3.put(relatedEntityMap.get(entityCode).getParentCode(), new RelatedEntityDTO());
            // 子级
            List<String> childrenLis = relatedEntityMap.get(entityCode).getChildrenList();
            for (String children : childrenLis) {
                if (relatedEntityMap.get(entityCode) != null) {
                    map3.put(children, new RelatedEntityDTO());
                    relatedEntityMap.remove(children);
                }
            }

            // 清除已经添加的

            relatedEntityMap.remove(relatedEntityMap.get(entityCode).getParentCode());
            relatedEntityMap.remove(entityCode);
        }
        levelMap.put("3", map3);

        // 二级
        LinkedHashMap<String, RelatedEntityDTO> map2 = new LinkedHashMap<>();
        map2.put("001", new RelatedEntityDTO());
        if (relatedEntityMap.get("001") != null) {
            relatedEntityMap.remove("001");
        }
        levelMap.put("2", map2);

        // 一级
        levelMap.put("1", relatedEntityMap);

        log.info("返回的实体相关levelMap为:{}", JSON.toJSONString(levelMap));

        return levelMap;
    }

    private Map<String, LastPosition> getLastPosition(String uid) {
        String key = getLastPositionUidRedisKey(uid);
        Long size = redisTemplate.opsForZSet().size(key);
        Set<Object> set = redisTemplate.opsForZSet().range(key, 0, size);

        List<LastPosition> list = set.stream().map(o -> {
            return (LastPosition)o;
        }).collect(Collectors.toList());
        return list.stream()
            .collect(Collectors.toMap(LastPosition::getEntityCode, value -> value, (oldVal, curVal) -> curVal));
    }

    private void saveLastPosition(String uid, Map<String, LastPosition> map) {
        String key = this.getLastPositionUidRedisKey(uid);
        redisTemplate.delete(key);
        for (LastPosition lp : map.values()) {
            redisTemplate.opsForZSet().add(key, lp, 0D);
        }
        redisTemplate.expire(key, 600, TimeUnit.SECONDS);
    }

    private List<String> getEntityListByUid(String uid) {
        UserEntityDTO userEntityDTO = new UserEntityDTO();
        userEntityDTO.setUid(uid);
        userEntityDTO.setIsDelete((byte)0);
        List<UserEntity> ueList = userEntityMapper.queryByCondition(userEntityDTO);
        List<String> entityList = Lists.newArrayList();
        for (UserEntity userEntity : ueList) {
            entityList.add(userEntity.getEntityCode());
        }
        return entityList;
    }

    public static void main(String[] args) {
        Integer aInteger = Integer.valueOf("1000");
        Integer bInteger = (int)(aInteger / 2.5);
        System.out.print(bInteger);
    }
}


