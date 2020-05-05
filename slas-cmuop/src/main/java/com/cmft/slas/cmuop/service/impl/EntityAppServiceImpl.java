package com.cmft.slas.cmuop.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.cmft.slas.cmuop.common.constant.RedisConstant;
import com.cmft.slas.cmuop.dto.EntitySentimentCountDTO;
import com.cmft.slas.cmuop.processor.processorImpl.EntityProcessor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.cmft.slas.cmuop.entity.CmuopEntity;
import com.cmft.slas.cmuop.entity.UserEntity;
import com.cmft.slas.cmuop.mapper.ArticleRelatedEntityMapper;
import com.cmft.slas.cmuop.mapper.CmuopEntityMapper;
import com.cmft.slas.cmuop.mapper.UserEntityMapper;
import com.cmft.slas.cmuop.service.EntityAppService;
import com.cmft.slas.cmuop.vo.EntityTrendVO;
import com.cmft.slas.cmuop.vo.EntityVO;

import tk.mybatis.mapper.entity.Example;

/**
 * @Author liurp001
 * @Since 2020/1/2
 */
@Service
public class EntityAppServiceImpl implements EntityAppService {
    @Autowired
    private CmuopEntityMapper cmuopEntityMapper;

    @Autowired
    private UserEntityMapper userEntityMapper;

    @Autowired
    private ArticleRelatedEntityMapper articleRelatedEntityMapper;

    @Autowired
    private EntityProcessor entityProcessor;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${cmuop.expireTime}")
    private String expireTimeStr;

    @Override
    public List<EntityVO> getAllEntities(String uid) {
        List<CmuopEntity> entityList = getEntitiesFromRedis(uid);
        List<EntityVO> voList = new ArrayList<>();
        voList = entityList.stream().map(o -> {
            EntityVO eVO = new EntityVO();
            BeanUtils.copyProperties(o, eVO);
            return eVO;
        }).collect(Collectors.toList());
        return voList;
    }

    @SuppressWarnings("unchecked")
    private List<CmuopEntity> getEntitiesFromRedis(String uid) {
        List<CmuopEntity> entityList = (List<CmuopEntity>) redisTemplate.opsForValue().get(getTreeKey());
        if (CollectionUtils.isEmpty(entityList)) {
//            entityList = entityProcessor.getAllEntities();
            entityList = entityProcessor.getAllEntitiesByOrderNum();
            Long timeout = Long.valueOf(expireTimeStr);
            redisTemplate.opsForValue().set(getTreeKey(), entityList, timeout, TimeUnit.SECONDS);
        }
//        return entityProcessor.generateCompletetree(entityList, uid);
        return entityProcessor.generateEntityList(entityList, uid);
    }

    private String getTreeKey() {
        return "slas:cmuop:entity_tree";
    }

    @Override
    public List<EntityTrendVO> getEntityStatWRTUser(String uid, Integer dateBackNum) {
        // @TODO optimization needed !!!!!
        List<String> entityCodes =
            getAllEntities(uid).stream().map(EntityVO::getEntityCode).limit(6).collect(Collectors.toList());
        List<EntityTrendVO> entityStats = new ArrayList<>();
        for (String code : entityCodes) {
            entityStats.add(getEntityStat(code, dateBackNum));
        }
        return entityStats;
    }

    @Override
    public List<String> getAllUserEntityCode(String uid) {
        List<String> userEntity = entityProcessor.getUserEntityList(uid);
        List<CmuopEntity> treeOrder = getEntitiesFromRedis(uid);
        Map<String, CmuopEntity> findParentMap = treeOrder.stream().collect(Collectors.toMap(CmuopEntity::getEntityCode, value -> value));
        List<String> resultList = new ArrayList<>(userEntity);
        userEntity.forEach(entityCode -> {
            String code = entityCode;
            while (true) {
                CmuopEntity entity = findParentMap.get(code);
                if (entity == null)
                    break;
                String parentCode = entity.getParentCode();
                resultList.add(parentCode);
                code = parentCode;
            }
        });
        List<String> distictCode = resultList.stream().distinct().collect(Collectors.toList());
        return treeOrder.stream().map(CmuopEntity::getEntityCode).filter(distictCode::contains).collect(Collectors.toList());
    }

    @Override
    public String reloadEntityList() {
        redisTemplate.delete(getTreeKey());
        redisTemplate.delete(RedisConstant.ENTITY_LIST_KEY);
        return "done";
    }

    @Override
    public EntityTrendVO getEntityStat(String entityCode, Integer dateBackNum) {
        CmuopEntity entity = cmuopEntityMapper.selectOne(new CmuopEntity().setEntityCode(entityCode));
        if (entity == null)
            return new EntityTrendVO();
        EntityTrendVO trend = new EntityTrendVO().setEntityCode(entityCode).setEntityName(entity.getEntityName());
        List<EntitySentimentCountDTO> count = articleRelatedEntityMapper.getEntityStat(entityCode, dateBackNum);
        Map<Integer, Integer> sentiMap = count.stream().collect(Collectors.toMap(EntitySentimentCountDTO::getSentiment, EntitySentimentCountDTO::getCount));
        Integer positive = sentiMap.getOrDefault(Sentiment.POSITIVE.sentiment, 0);
        Integer neutral = sentiMap.getOrDefault(Sentiment.NEUTRAL.sentiment, 0);
        Integer negative = sentiMap.getOrDefault(Sentiment.NEGATIVE.sentiment, 0);
        trend.setNegative(negative).setPositive(positive).setTotal(negative + neutral + positive);
        return trend;
    }

    private enum Sentiment {
        POSITIVE(1), NEUTRAL(0), NEGATIVE(-1);
        private Integer sentiment;

        Sentiment(Integer sentiment) {
            this.sentiment = sentiment;
        }
    }
}
