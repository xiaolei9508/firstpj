package com.cmft.slas.cmuop.service;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import com.cmft.slas.cmuop.dto.AllArticleDTO;
import com.cmft.slas.cmuop.dto.StatDTO;
import com.cmft.slas.cmuop.entity.Article;
import com.cmft.slas.cmuop.vo.AllArticleDetail;
import com.cmft.slas.cmuop.vo.AllArticlePreview;
import com.cmft.slas.cmuop.vo.ArticleDetail;
import com.cmft.slas.cmuop.vo.EntityStat;
import com.cmft.slas.common.pojo.Page;
import com.github.pagehelper.PageInfo;

/**
 * @Author liurp001
 * @Since 2019/12/30
 */
public interface AllArticleWebService {
    PageInfo<AllArticlePreview> getArticleList(Page page, Date dateFrom, Date dateTo, String title, StatDTO statDTO, Boolean ifStick);

    AllArticleDetail getArticleDetail(String articleId);

    String updateArticle(String articleId, AllArticleDTO allArticleDTO)
        throws IllegalAccessException, InvocationTargetException;

    EntityStat getEntityStat(Date dateFrom, Date dateTo, String title, StatDTO statDTO);

    Article getArticleByArticleId(String articleId);

    Integer updateArticleByPK(Article article);

    void makeUpAllArticleDetail(String articleId, ArticleDetail ArticleDetail);

    InputStream exportExcel(Date dateFrom, Date dateTo, String title, StatDTO statDTO, Boolean ifStick);

    void resetOrderNum(String articleId, boolean isAll);

}
