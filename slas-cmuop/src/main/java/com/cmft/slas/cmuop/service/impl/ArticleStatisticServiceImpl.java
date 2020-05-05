package com.cmft.slas.cmuop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmft.slas.cmuop.common.utils.BeanMapper;
import com.cmft.slas.cmuop.entity.ArticleStatistic;
import com.cmft.slas.cmuop.mapper.ArticleStatisticMapper;
import com.cmft.slas.cmuop.service.ArticleStatisticService;

@Service("articleStatisticService")
public class ArticleStatisticServiceImpl implements ArticleStatisticService {
    @Autowired
    private ArticleStatisticMapper articleStatisticMapper;
    @Autowired
    private BeanMapper beanMapper;

    @Override
    public ArticleStatistic selectOne(ArticleStatistic articleStatistic) {
        return articleStatisticMapper.selectOne(articleStatistic);
    }

    @Override
    public int updateArticleStatistic(ArticleStatistic articleStatistic) {
        return articleStatisticMapper
            .updateByPrimaryKeySelective(beanMapper.map(articleStatistic, ArticleStatistic.class));
    }

    @Override
    public int addArticleStatistic(ArticleStatistic articleStatistic) {
        return articleStatisticMapper.insertSelective(beanMapper.map(articleStatistic, ArticleStatistic.class));
    }
}
