package com.cmft.slas.cmuop.service;

import java.util.List;

import com.cmft.slas.es.domain.ArticleContent;

/**
 * @Author liurp001
 * @Since 2019/12/31
 */
public interface SearchFromESService {
    List<String> searchByTitle(String title);

    ArticleContent getArticle(String articleId);

    boolean updateArticle(ArticleContent articleContent);
}
