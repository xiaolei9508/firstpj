package com.cmft.slas.cmuop.service;

import com.cmft.slas.cmuop.vo.AppArticlePreview;
import com.cmft.slas.common.pojo.PageInfo;

public interface ArticleListSortService {

    PageInfo<AppArticlePreview> getSortedArticleList(String uid, String entityType, String sentimentType,
        String columnType, String lastPosition, Integer pageSize, Boolean ifFirstTime);

}
