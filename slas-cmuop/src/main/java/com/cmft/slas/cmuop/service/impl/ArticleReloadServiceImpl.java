package com.cmft.slas.cmuop.service.impl;

import java.util.List;

import com.cmft.slas.cmuop.common.constant.ColumnTypes;
import com.cmft.slas.cmuop.common.constant.FakeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.cmft.slas.cmuop.common.service.RedisLockService;
import com.cmft.slas.cmuop.dto.ArticleForSortDTO;
import com.cmft.slas.cmuop.dto.ArticleRelatedEntityDTO;
import com.cmft.slas.cmuop.entity.Article;
import com.cmft.slas.cmuop.entity.ArticleRelatedEntity;
import com.cmft.slas.cmuop.entity.CmuopEntity;
import com.cmft.slas.cmuop.mapper.ArticleMapper;
import com.cmft.slas.cmuop.mapper.ArticleRelatedEntityMapper;
import com.cmft.slas.cmuop.mapper.CmuopEntityMapper;
import com.cmft.slas.cmuop.processor.processorImpl.ArticleSortPrepareProcessor;
import com.cmft.slas.cmuop.service.ArticleReloadService;
import com.cmft.slas.common.utils.BeanMapper;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

@Service
@Slf4j
public class ArticleReloadServiceImpl implements ArticleReloadService {

    @Value("${cmuop.mNumber}")
    private String mNumber;

    @Value("${cmuop.nNumber}")
    private String nNumber;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleRelatedEntityMapper articleRelatedEntityMapper;

    @Autowired
    private ArticleSortPrepareProcessor articleSortPrepareProcessor;

    @Autowired
    private CmuopEntityMapper cmuopEntityMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisLockService redisLockService;

    @Override
    public void reload() {
        reloadEntity();
    }

    @Override
    public void reload(Article article) {
        ArticleForSortDTO newArticle = BeanMapper.map(article, ArticleForSortDTO.class);
        articleSortPrepareProcessor.process(newArticle);
        reloadEntity(newArticle);
    }

    private void reloadEntity(ArticleForSortDTO article) {
        Example example = new Example(CmuopEntity.class);
        example.createCriteria().andEqualTo("ifCmuop", true).andEqualTo("isDelete", 0);
        List<CmuopEntity> entityList = cmuopEntityMapper.selectByExample(example);

        for (CmuopEntity ce : entityList) {
            log.info("开始同步{}的数据到redis", ce.getEntityCode());
            removeEntity(ce.getEntityCode(), article);
        }
    }

    private void reloadEntity() {
        Example example = new Example(CmuopEntity.class);
        example.createCriteria().andEqualTo("ifCmuop", true).andEqualTo("isDelete", 0);
        List<CmuopEntity> entityList = cmuopEntityMapper.selectByExample(example);

        for (CmuopEntity ce : entityList) {
            log.info("开始同步{}的数据到redis", ce.getEntityCode());
            removeEntity(ce.getEntityCode());
            saveEntity(ce.getEntityCode());
        }
    }

    private void removeEntity(String entityType) {
        redisTemplate.delete(getEntityKey(entityType));
    }

    private void removeEntity(String entityType, ArticleForSortDTO article) {
        // 获取entityKey
        String entityKey = getEntityKey(entityType);
        long count = redisTemplate.opsForZSet().removeRangeByScore(entityKey, -article.getPubTime().getTime(),
                -article.getPubTime().getTime());
    }

    private String getEntityKey(String entityCode) {
        return String.format("slas:cmuop:article:entity:%s", entityCode);
    }

    private void saveEntity(String entityCode) {
        Integer m = Integer.valueOf(mNumber);
        List<ArticleForSortDTO> articleList = articleMapper.selectArticleListByType(null, entityCode, null, m);

        articleSortPrepareProcessor.process(articleList);
        for (int i = 1; i <= articleList.size(); i++) {
            redisTemplate.opsForZSet().add(getEntityKey(entityCode), articleList.get(i - 1),
                    -articleList.get(i - 1).getPubTime().getTime());
        }

        log.info("共同步{}条数据至{} redis", articleList.size(), entityCode);
    }

    @Override
    public void ifUpdateThumbnail(Boolean ifUpdate) {
        redisTemplate.opsForValue().set("if_update_thumbnail", ifUpdate);
    }
}
