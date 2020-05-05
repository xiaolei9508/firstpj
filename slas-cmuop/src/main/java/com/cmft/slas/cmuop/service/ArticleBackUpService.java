package com.cmft.slas.cmuop.service;

import com.cmft.slas.cmuop.entity.Article;

public interface ArticleBackUpService {

    void backUpArticleToRedis(Article article);
}
