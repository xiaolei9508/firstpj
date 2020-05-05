package com.cmft.slas.cmuop.service;

import java.util.List;

import com.cmft.slas.cmuop.vo.AppArticlePreview;

public interface ArticleListForAppService {

    List<AppArticlePreview> getArticleListForCarousels(String uid);

}
