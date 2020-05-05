package com.cmft.slas.cmuop.service;

import com.cmft.slas.cmuop.entity.Article;

public interface ArticleReloadService {

    void reload();

    /**
     * 从redis各实体缓存中去除指定文章 requestparam：articleId, pubTime
     * 
     * @param article
     */
    void reload(Article article);

    void ifUpdateThumbnail(Boolean ifUpdate);
}
