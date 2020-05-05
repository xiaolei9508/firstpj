package com.cmft.slas.cmuop.service;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import com.cmft.slas.cmuop.dto.SelectedArticleDTO;
import com.cmft.slas.cmuop.vo.EntityStat;
import com.cmft.slas.cmuop.vo.SelectedDetail;
import com.cmft.slas.cmuop.vo.SelectedPreview;
import com.cmft.slas.common.pojo.Page;
import com.github.pagehelper.PageInfo;

/**
 * @Author liurp001
 * @Since 2019/12/27
 */
public interface SelectedArticleWebService {

    PageInfo<SelectedPreview> getArticleList(Page page, Date dateFrom, Date dateTo, String title);

    SelectedDetail getArticleDetail(String articleId);

    String updateArticle(String articleId, SelectedArticleDTO SelectedArticleDTO)
        throws IllegalAccessException, InvocationTargetException;

    EntityStat getEntityStat(String articleId, String title, Date date);

    InputStream exportExcel(Date dateFrom, Date dateTo, String title);
}
