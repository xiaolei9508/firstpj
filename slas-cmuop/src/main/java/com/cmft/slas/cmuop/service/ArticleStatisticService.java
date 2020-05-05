package com.cmft.slas.cmuop.service;

import com.cmft.slas.cmuop.entity.ArticleStatistic;

public interface ArticleStatisticService {

    ArticleStatistic selectOne(ArticleStatistic articleStatistic);

    int updateArticleStatistic(ArticleStatistic articleStatistic);

    int addArticleStatistic(ArticleStatistic articleStatistic);

}
