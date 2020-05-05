package com.cmft.slas.cmuop.service.impl;

import com.cmft.slas.cmuop.common.constant.ColumnTypes;
import com.cmft.slas.cmuop.common.constant.Constant;
import com.cmft.slas.cmuop.common.constant.FakeEntity;
import com.cmft.slas.cmuop.common.constant.WebSortType;
import com.cmft.slas.cmuop.common.utils.BeanMapper;
import com.cmft.slas.cmuop.dto.*;
import com.cmft.slas.cmuop.entity.*;
import com.cmft.slas.cmuop.mapper.*;
import com.cmft.slas.cmuop.processor.ArticlePreviewProcessor;
import com.cmft.slas.cmuop.processor.processorImpl.LeaderArticleProcessor;
import com.cmft.slas.cmuop.processor.processorImpl.NotificationDistributor;
import com.cmft.slas.cmuop.service.*;
import com.cmft.slas.cmuop.vo.*;
import com.cmft.slas.common.pojo.Page;
import com.cmft.slas.es.domain.ArticleContent;
import com.cmft.slas.recommendation.common.service.RedisService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author liurp001
 * @Since 2019/12/30
 */
@Service
public class AllArticleWebServiceImpl implements AllArticleWebService {

    @Autowired
    private ArticleMapper articleMapper ;

    @Autowired
    private SearchFromESService searchFromESService;

    @Autowired
    private CmuopEntityMapper cmuopEntityMapper;

    @Autowired
    private ArticleRelatedEntityMapper articleRelatedEntityMapper;

    @Autowired
    private ArticlePreviewProcessor processors;

	@Autowired
    private ExcelService excelService;

    @Autowired
    private CmuopEntityService cmuopEntityService;

    @Autowired
    private BeanMapper beanMapper;

    @Autowired
    private ArticleBackUpService articleBackUpService;

    @Autowired
    private ArticleReloadService articleReloadService;

    @Autowired
    private CmuopDictItemMapper cmuopDictItemMapper;

    @Autowired
    private ArticleForAppService articleForAppService;

    @Autowired
    private ArticleRelatedEntityService articleRelatedEntityService;

    @Autowired
    private LeaderArticleProcessor leaderArticleProcessor;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ArticleLabelMapper articleLabelMapper;

    @Autowired
    private NotificationDistributor notificationDistributor;

    @Override
    public PageInfo<AllArticlePreview> getArticleList(Page page, Date dateFrom, Date dateTO, String title,
        StatDTO statDTO, Boolean ifStick) {
        List<String> titleIds = Collections.emptyList();
        if(StringUtils.isNotBlank(title)){
            titleIds = searchFromESService.searchByTitle(title.trim());
            if(CollectionUtils.isEmpty(titleIds))
                return new PageInfo<>();
        }
        List<AllArticlePreview> allPreview = Collections.EMPTY_LIST;
        PageHelper.startPage(page == null ? 1 : page.getPageNum(), page == null ? 10 : page.getPageSize());
        allPreview = articleMapper.getAllArticleViaJoin(dateFrom,
                dateTO,
                CollectionUtils.isEmpty(titleIds) ? null : titleIds,
                statDTO,
                CollectionUtils.isEmpty(statDTO.getEntityStatList()) ? null : statDTO.getEntityStatList().get(0),
                CollectionUtils.isEmpty(statDTO.getThirdLevelStatList()) ? null : statDTO.getThirdLevelStatList().get(0),
                page == null ? null : getOrderBy(page),
                ifStick);
        PageInfo<AllArticlePreview> pageInfo = new PageInfo<>(allPreview);
        processors.processPreview(pageInfo.getList());
        return pageInfo;
    }

    enum Column {
        RECOMMEND("0");
        String code;
        Column(String code){
            this.code = code;
        }
    }

    private boolean checkIfAllEntity(StatDTO statDTO){
        return CollectionUtils.isEmpty(statDTO.getEntityStatList()) || statDTO.getEntityStatList().size() > 1;
    }

    private String getOrderBy(Page page){
        if(page.getOrderBy() != null && page.getColumnName() != null){
            return WebSortType.getValue(page.getColumnName())+ " " + page.getOrderBy();
        }
        return null;
    }

    private List<String> getEntityList(StatDTO statDTO){
        List<String> fieldList = Collections.emptyList();
        if(CollectionUtils.isNotEmpty(statDTO.getEntityStatList())){
            Example entityExample = new Example(CmuopEntity.class);
            entityExample
                    .createCriteria()
                    .andIn("entityCode", statDTO.getEntityStatList());
            fieldList = cmuopEntityMapper.selectByExample(entityExample)
                    .stream()
                    .map(CmuopEntity::getIndustryId)
                    .collect(Collectors.toList());
        }
        return fieldList;
    }

    @Override
    public AllArticleDetail getArticleDetail(String articleId) {
        return null;
    }

    @Override
    public String updateArticle(String articleId, AllArticleDTO allArticleDTO)
        throws IllegalAccessException, InvocationTargetException {
        Article article = articleMapper.getArticleByArticleId(articleId);
        if (article == null) {
            return "未找到该文章";
        }
        Article newArticle = beanMapper.map(allArticleDTO, Article.class);
        newArticle.setTArticleId(article.getTArticleId());
        newArticle.setUpdateTime(new Date());
        articleMapper.updateByPrimaryKeySelective(newArticle);

        if (CollectionUtils.isNotEmpty(allArticleDTO.getArticleRelatedEntityColumnDTOList())) {
            Example example = new Example(ArticleRelatedEntity.class);
            Criteria criteria = example.createCriteria();
            criteria.andEqualTo("articleId", articleId).andNotEqualTo("columnType","0");
            articleRelatedEntityMapper.deleteByExample(example);
            boolean ifEntityExists = false;
            for (ArticleRelatedEntityColumnDTO dto : allArticleDTO.getArticleRelatedEntityColumnDTOList()) {
                if(!FakeEntity.ZHAOWEN.getCode().equals(dto.getEntityCode())
                        && !ColumnTypes.RECOMMEND.getValue().equals(dto.getColumnType())
                        && !ColumnTypes.INDUSTRY.getValue().equals(dto.getColumnType())) {
                    ifEntityExists = true;
                }
                ArticleRelatedEntity articleRelatedEntity = new ArticleRelatedEntity();
                articleRelatedEntity.setArticleId(articleId);
                articleRelatedEntity.setEntityCode(dto.getEntityCode());
                articleRelatedEntity.setColumnType(dto.getColumnType());
                if(ColumnTypes.LEADER.getValue().equals(dto.getColumnType()) && !article.getIfPartStick()){
                    articleRelatedEntity.setOrderNum(leaderArticleProcessor.updateArticleOrderNum(articleId, dto.getEntityCode()));
                }else{
                    articleRelatedEntity.setOrderNum(ObjectUtils.firstNonNull(dto.getOrderNum(), 99));
                }
                articleRelatedEntityMapper.insertSelective(articleRelatedEntity);
            }
            //process with ifRecommend & ifAllStick
            if(!ifEntityExists){
                if(article.getIfRecommend()) {
                    newArticle.setIfRecommend(false);
                    articleRelatedEntityService.batchProcessEntityWithRecommend(articleId, false);
                }
                if(article.getIfAllStick()) {
                    newArticle.setIfAllStick(false);
                    resetOrderNum(articleId, true);
                }
            }
        }
        articleMapper.updateByPrimaryKeySelective(newArticle);

        ArticleContent articleContent = beanMapper.map(allArticleDTO, ArticleContent.class);
        articleContent.setArticleId(articleId);
        searchFromESService.updateArticle(articleContent);
        articleReloadService.reload(article);
        article = articleMapper.getArticleByArticleId(articleId);
        if (articleForAppService.checkIfSaveInRedis(article)) {
            articleBackUpService.backUpArticleToRedis(article);
        }
        if(StringUtils.isNotBlank(articleContent.getContentHtml())){
            String cacheKey = String.format("slas:cmuop:article:contentHtml:%s", article.getArticleId());
            redisService.setString(cacheKey, articleContent.getContentHtml(), 3L, TimeUnit.DAYS);
        }
        notificationDistributor.sendNotification(article, getArticleLabels(articleId));
        return "更新成功";

    }

    private List<ArticleLabel> getArticleLabels(String articleId){
        Example example = new Example(ArticleLabel.class);
        example.createCriteria().andEqualTo("articleId", articleId).andEqualTo("isDelete", (byte)0);
        return articleLabelMapper.selectByExample(example);
    }

    @Override
    public EntityStat getEntityStat(Date dateFrom, Date dateTo, String title, StatDTO statDTO) {
        List<String> titleIds = Collections.emptyList();
        EntityStat entityStat = new EntityStat();
        if (StringUtils.isNotBlank(title)) {
            titleIds = searchFromESService.searchByTitle(title);
        }
        Map<Integer, String> ifShowMap = new HashMap<>();
        ifShowMap.put(0, "不显示");
        ifShowMap.put(1, "显示");
        Map<Integer, String> sentimentMap = new HashMap<>();
        sentimentMap.put(-1, "负面");
        sentimentMap.put(0, "中性");
        sentimentMap.put(1, "正面");
        Map<String, String> entityMap = cmuopEntityService.getCmuopEntityMap();
        entityMap.put(Constant.ZHAOWEN, Constant.ZHAOWENNAME);
        Map<String, String> typeMap = this.getTypeMap();
        List<String> fieldList = getEntityList(statDTO);
        List<CountDTO> ifShowStatList =
            articleMapper.countIfshow(dateFrom, dateTo, CollectionUtils.isEmpty(titleIds) ? null : titleIds,
                CollectionUtils.isEmpty(fieldList) ? null : fieldList, statDTO,
                CollectionUtils.isEmpty(statDTO.getEntityStatList()) ? null : statDTO.getEntityStatList().get(0),
                CollectionUtils.isEmpty(statDTO.getThirdLevelStatList()) ? null
                    : statDTO.getThirdLevelStatList().get(0));
        List<InfoStatVO> entityStatList =
            articleMapper.countEntityArticle(dateFrom, dateTo, CollectionUtils.isEmpty(titleIds) ? null : titleIds,
                CollectionUtils.isEmpty(fieldList) ? null : fieldList, statDTO,
                CollectionUtils.isEmpty(statDTO.getEntityStatList()) ? null : statDTO.getEntityStatList().get(0),
                CollectionUtils.isEmpty(statDTO.getThirdLevelStatList()) ? null
                    : statDTO.getThirdLevelStatList().get(0));
        List<InfoStatVO> thirdLevelStatList = 
            articleMapper.countTypeArticle(dateFrom, dateTo, CollectionUtils.isEmpty(titleIds) ? null : titleIds,
                CollectionUtils.isEmpty(fieldList) ? null : fieldList, statDTO,
                CollectionUtils.isEmpty(statDTO.getEntityStatList()) ? null : statDTO.getEntityStatList().get(0),
                CollectionUtils.isEmpty(statDTO.getThirdLevelStatList()) ? null
                    : statDTO.getThirdLevelStatList().get(0));
        List<CountDTO> sentimentStatList =
            articleMapper.countSentimentArticle(dateFrom, dateTo, CollectionUtils.isEmpty(titleIds) ? null : titleIds,
                CollectionUtils.isEmpty(fieldList) ? null : fieldList, statDTO,
                CollectionUtils.isEmpty(statDTO.getEntityStatList()) ? null : statDTO.getEntityStatList().get(0),
                CollectionUtils.isEmpty(statDTO.getThirdLevelStatList()) ? null
                    : statDTO.getThirdLevelStatList().get(0));
        entityStat.setIfShowStatList(this.setName(ifShowStatList, ifShowMap));
        entityStat.setSentimentStatList(this.setName(sentimentStatList, sentimentMap));
        entityStat.setEntityStatList(this.setStatName(entityStatList, entityMap));
        entityStat.setThirdLevelStatList(this.setStatName(thirdLevelStatList, typeMap));
        return entityStat;
    }

    @Override
    public Article getArticleByArticleId(String articleId) {
        return articleMapper.getArticleByArticleId(articleId);
    }

    @Override
    public Integer updateArticleByPK(Article article) {
        int count = articleMapper.updateByPrimaryKeySelective(article);
        notificationDistributor.sendNotification(article, getArticleLabels(article.getArticleId()));
        return count;
    }

    @Override
    public void makeUpAllArticleDetail(String articleId, ArticleDetail articleDetail) {
        ArticleContent articleContext = searchFromESService.getArticle(articleId);
        if (articleContext != null) {
            articleDetail.setContentHtml(articleContext.getContentHtml());
        }
        ArticleRelatedEntityDTO articleRelatedEntityDTO = new ArticleRelatedEntityDTO();
        articleRelatedEntityDTO.setArticleId(articleId);
        List<ArticleRelatedEntity> list = articleRelatedEntityMapper.queryByCondition(articleRelatedEntityDTO);
        List<InfoStatVO> relatedEntityList = new ArrayList<>();

        CmuopDictItemDTO cmuopDictItemDTO = new CmuopDictItemDTO();
        cmuopDictItemDTO.setDictCode("field_level");
        List<CmuopDictItem> cmuopDictItemList = cmuopDictItemMapper.queryByCondition(cmuopDictItemDTO);
        Map<String, String> dictMap = cmuopDictItemList.stream().collect(Collectors.toMap(CmuopDictItem::getValue, CmuopDictItem::getText));

        List<ArticleRelatedEntityColumnDTO> articleRelatedEntityColumnDTOList = new ArrayList<>();

        for (ArticleRelatedEntity articleRelatedEntity : list) {
            // 文章分类过滤推荐的
            if(StringUtils.isBlank(articleRelatedEntity.getColumnType()) || "0".equals(articleRelatedEntity.getColumnType())){
                continue;
            }
            CmuopEntityDTO entityDTO = new CmuopEntityDTO();
            entityDTO.setEntityCode(articleRelatedEntity.getEntityCode());
            entityDTO.setIfCmuop((byte)1);
            if("000".equals(entityDTO.getEntityCode())) {
                ArticleRelatedEntityColumnDTO dto = new ArticleRelatedEntityColumnDTO();
                dto.setArticleEntityId(articleRelatedEntity.getArticleEntityId());
                dto.setArticleId(articleRelatedEntity.getArticleId());
                dto.setEntityCode(articleRelatedEntity.getEntityCode());
                dto.setEntityName("招闻天下");
                dto.setColumnType(articleRelatedEntity.getColumnType());
                dto.setColumnName(dictMap.get(articleRelatedEntity.getColumnType()));
                dto.setOrderNum(articleRelatedEntity.getOrderNum());
                articleRelatedEntityColumnDTOList.add(dto);
            } else {
                List<CmuopEntity> entityList = cmuopEntityMapper.queryByCondition(entityDTO);
                if (entityList.size() > 0) {
                    CmuopEntity entity = entityList.get(0);
                    InfoStatVO info = new InfoStatVO();
                    info.setCode(entity.getEntityCode());
                    info.setName(entity.getEntityName());
                    relatedEntityList.add(info);

                    ArticleRelatedEntityColumnDTO dto = new ArticleRelatedEntityColumnDTO();
                    dto.setArticleEntityId(articleRelatedEntity.getArticleEntityId());
                    dto.setArticleId(articleRelatedEntity.getArticleId());
                    dto.setEntityCode(articleRelatedEntity.getEntityCode());
                    dto.setEntityName(entity.getEntityName());
                    dto.setColumnType(articleRelatedEntity.getColumnType());
                    dto.setColumnName(dictMap.get(articleRelatedEntity.getColumnType()));
                    dto.setOrderNum(articleRelatedEntity.getOrderNum());
                    articleRelatedEntityColumnDTOList.add(dto);
                }
            }
        }
        articleDetail.setRelatedEntityList(relatedEntityList);
        articleDetail.setArticleRelatedEntityColumnDTOList(articleRelatedEntityColumnDTOList);

    }

    @Override
    public InputStream exportExcel(Date dateFrom, Date dateTo, String title, StatDTO statDTO, Boolean ifStick) {
        List<String> titleIds = Collections.emptyList();
        if (StringUtils.isNotBlank(title)) {
            titleIds = searchFromESService.searchByTitle(title.trim());
            if (CollectionUtils.isEmpty(titleIds))
                return excelService.writeExcel(new ArrayList<AllArticlePreview>(), dateFrom, dateTo,
                    cmuopEntityService.getCmuopEntityMap());
        }
        List<AllArticlePreview> allPreview = Collections.EMPTY_LIST;
        allPreview = articleMapper.getAllArticleViaJoin(dateFrom, dateTo,
            CollectionUtils.isEmpty(titleIds) ? null : titleIds, statDTO,
            CollectionUtils.isEmpty(statDTO.getEntityStatList()) ? null : statDTO.getEntityStatList().get(0),
            CollectionUtils.isEmpty(statDTO.getThirdLevelStatList()) ? null : statDTO.getThirdLevelStatList().get(0),
            null,ifStick);
        processors.processPreview(allPreview);
        return excelService.writeExcel(allPreview, dateFrom, dateTo, cmuopEntityService.getCmuopEntityMap());
    }

    private Map<String, String> getTypeMap() {
        CmuopDictItemDTO params = new CmuopDictItemDTO();
        params.setDictCode("three_level");
        params.setIsDelete((byte)0);
        List<CmuopDictItem> list = cmuopDictItemMapper.queryByCondition(params);
        Map<String, String> typeMap = new HashMap<>();
        for (CmuopDictItem type : list) {
            typeMap.put(type.getValue(), type.getText());
        }
        return typeMap;
    }

    private List<InfoStatVO> setName(List<CountDTO> list, Map<Integer, String> map) {
        List<InfoStatVO> statList = new ArrayList<>();
        for (CountDTO count : list) {
            InfoStatVO info = new InfoStatVO();
            Integer code = count.getCode();
            if (map.containsKey(code)) {
                info.setCode(String.valueOf(code));
                info.setCount(count.getCount());
                info.setName(map.get(code));
                map.remove(code);
                statList.add(info);
            }
        }
        for (Integer key : map.keySet()) {
            InfoStatVO info = new InfoStatVO();
            info.setCode(String.valueOf(key));
            info.setCount(0);
            info.setName(map.get(key));
            statList.add(info);
        }
        return statList;
    }

    private List<InfoStatVO> setStatName(List<InfoStatVO> list, Map<String, String> map) {
        List<InfoStatVO> statList = new ArrayList<>();
        for (InfoStatVO info : list) {
            String code = info.getCode();
            if (map.containsKey(code)) {
                info.setCode(code);
                info.setCount(info.getCount());
                info.setName(map.get(code));
                statList.add(info);
                map.remove(code);
            }
        }
        for (String key : map.keySet()) {
            if (key.equals("0")) {
                continue;
            }
            InfoStatVO info = new InfoStatVO();
            info.setCode(String.valueOf(key));
            info.setCount(0);
            info.setName(map.get(key));
            statList.add(info);
        }
        return statList;
    }

    @Override
    public void resetOrderNum(String articleId, boolean isAll) {
        Example example = new Example(ArticleRelatedEntity.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("articleId", articleId);
        if(isAll) {
            criteria.andEqualTo("columnType","0");
        } else {
            criteria.andNotEqualTo("columnType","0");
        }
        ArticleRelatedEntity articleRelatedEntity = new ArticleRelatedEntity();
        articleRelatedEntity.setOrderNum(99);
        articleRelatedEntityMapper.updateByExampleSelective(articleRelatedEntity, example);
    }
}
