package com.cmft.slas.cmuop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.cmft.slas.cmuop.common.service.RedisLockService;
import com.cmft.slas.cmuop.common.utils.TimeUtil;
import com.cmft.slas.cmuop.dto.ArticleForSortDTO;
import com.cmft.slas.cmuop.dto.ArticleRelatedEntityDTO;
import com.cmft.slas.cmuop.entity.Article;
import com.cmft.slas.cmuop.entity.ArticleRelatedEntity;
import com.cmft.slas.cmuop.mapper.ArticleRelatedEntityMapper;
import com.cmft.slas.cmuop.processor.processorImpl.ArticleSortPrepareProcessor;
import com.cmft.slas.cmuop.service.ArticleBackUpService;
import com.cmft.slas.common.utils.BeanMapper;

@Service
public class ArticleBackUpServiceImpl implements ArticleBackUpService {

    @Value("${cmuop.mNumber}")
    private String mNumber;

    @Autowired
    private ArticleRelatedEntityMapper articleRelatedEntityMapper;

    @Autowired
    private ArticleSortPrepareProcessor articleSortPrepareProcessor;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisLockService redisLockService;

    @Override
    public void backUpArticleToRedis(Article article) {
        ArticleForSortDTO newArticle = BeanMapper.map(article, ArticleForSortDTO.class);

        // 发布时间14天前的文章不予进入redis
        Integer timeInterval = TimeUtil.getTimeInterval(article.getPubTime());
        if (timeInterval > 14) {
            return;
        }

        articleSortPrepareProcessor.process(newArticle);
        saveToRedis(newArticle);
    }

    private void saveToRedis(ArticleForSortDTO article) {
        saveForEntity(article);
    }

    private void saveForEntity(ArticleForSortDTO article) {
        String articleId = article.getArticleId();
        ArticleRelatedEntityDTO areDTO = new ArticleRelatedEntityDTO();
        areDTO.setArticleId(articleId);
        List<ArticleRelatedEntity> areList = articleRelatedEntityMapper.selectDistinctEntity(areDTO);

        for (ArticleRelatedEntity entity : areList) {
            saveForEntity(entity.getEntityCode(), article);
        }
    }

    private void saveForEntity(String entityCode, ArticleForSortDTO article) {
        // 获取entityKey
        String entityKey = getEntityKey(entityCode);
        long size = redisTemplate.opsForZSet().size(entityKey);

        // 添加元素
        redisTemplate.opsForZSet().add(entityKey, article, -article.getPubTime().getTime());

        // 删除多余元素
        Integer n = Integer.valueOf(mNumber);
        size = redisTemplate.opsForZSet().size(entityKey);
        if (size > n) {
            redisTemplate.opsForZSet().removeRange(entityKey, n + 1, size);
        }
        // 删除Redis池中14天前的文章
        redisTemplate.opsForZSet().removeRangeByScore(entityKey,
                -TimeUtil.getDateIntervalDays(-14).getTime(),
                -TimeUtil.getDateIntervalDays(-100).getTime());
    }

    private String getEntityKey(String entityCode) {
        return String.format("slas:cmuop:article:entity:%s", entityCode);
    }

}
