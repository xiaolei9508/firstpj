package com.cmft.slas.cmuop.processor;

import java.util.List;

import com.cmft.slas.cmuop.dto.ArticleForSortDTO;

public interface ArticleSortProcessor {

    void process(ArticleForSortDTO article);

    void process(List<ArticleForSortDTO> articleList);

    void process(List<ArticleForSortDTO> articleList, String uid);

    void process(List<ArticleForSortDTO> articleList, String uid, String columnType);
}
