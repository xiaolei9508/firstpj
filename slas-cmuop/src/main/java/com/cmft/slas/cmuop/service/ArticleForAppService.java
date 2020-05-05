package com.cmft.slas.cmuop.service;

import com.cmft.slas.cmuop.entity.Article;
import com.cmft.slas.cmuop.vo.ArticleVO;

public interface ArticleForAppService {

    ArticleVO getArticleDetail(String articleId, String uid);

    Boolean checkIfSaveInRedis(Article article);
}
