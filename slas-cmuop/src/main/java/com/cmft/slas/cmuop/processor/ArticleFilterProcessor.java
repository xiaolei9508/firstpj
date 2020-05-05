package com.cmft.slas.cmuop.processor;

import java.util.List;

import com.cmft.slas.cmuop.dto.ArticleForSortDTO;
import com.cmft.slas.cmuop.vo.AppArticlePreview;

public interface ArticleFilterProcessor {

    List<AppArticlePreview> process(List<? extends AppArticlePreview> articleList, String uid, Boolean type);

    List<ArticleForSortDTO> process(String uid, List<ArticleForSortDTO> articleList);

    List<ArticleForSortDTO> process(String uid, List<ArticleForSortDTO> articleList, Boolean type);
}
