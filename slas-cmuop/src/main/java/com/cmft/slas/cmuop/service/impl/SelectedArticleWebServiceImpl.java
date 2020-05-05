package com.cmft.slas.cmuop.service.impl;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.cmft.slas.cmuop.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmft.slas.cmuop.common.constant.WebSortType;
import com.cmft.slas.cmuop.common.utils.BeanMapper;
import com.cmft.slas.cmuop.dto.ArticleRelatedEntityColumnDTO;
import com.cmft.slas.cmuop.dto.SelectedArticleDTO;
import com.cmft.slas.cmuop.entity.Article;
import com.cmft.slas.cmuop.entity.ArticleRelatedEntity;
import com.cmft.slas.cmuop.mapper.ArticleMapper;
import com.cmft.slas.cmuop.mapper.ArticleRelatedEntityMapper;
import com.cmft.slas.cmuop.mapper.CmuopEntityMapper;
import com.cmft.slas.cmuop.processor.ArticlePreviewProcessor;
import com.cmft.slas.cmuop.vo.AllArticlePreview;
import com.cmft.slas.cmuop.vo.EntityStat;
import com.cmft.slas.cmuop.vo.SelectedDetail;
import com.cmft.slas.cmuop.vo.SelectedPreview;
import com.cmft.slas.common.pojo.Page;
import com.cmft.slas.es.domain.ArticleContent;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * @Author liurp001
 * @Since 2019/12/30
 */
@Service
public class SelectedArticleWebServiceImpl implements SelectedArticleWebService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private SearchFromESService searchFromESService;

    @Autowired
    private ArticleRelatedEntityMapper articleRelatedEntityMapper;

    @Autowired
    private ArticlePreviewProcessor processor;

	@Autowired
    private ExcelService excelService;

    @Autowired
    private CmuopEntityMapper cmuopEntityMapper;

    @Autowired
    private CmuopEntityService cmuopEntityService;

    @Autowired
    private BeanMapper beanMapper;

    @Autowired
    private ArticleBackUpService articleBackUpService;

    @Autowired
    private ArticleReloadService articleReloadService;

    @Autowired
    private ArticleForAppService articleForAppService;

    @Override
    public PageInfo<SelectedPreview> getArticleList(Page page, Date dateFrom, Date dateTo, String title) {
        List<String> titleIds = Collections.emptyList();
        if(StringUtils.isNotBlank(title)){
            titleIds = searchFromESService.searchByTitle(title.trim());
            if(CollectionUtils.isEmpty(titleIds))
                return new PageInfo<>();
        }
        PageHelper.startPage(page == null ? 1 : page.getPageNum(), page == null ? 10 : page.getPageSize());
        List<SelectedPreview> selectedList = articleMapper.getSelectedViaJoin(dateFrom, dateTo,
                CollectionUtils.isEmpty(titleIds) ? null : titleIds,
                page == null ? null : getOrderBy(page));
        PageInfo<SelectedPreview> pageInfo = new PageInfo<>(selectedList);
        processor.processPreview(pageInfo.getList());
        return pageInfo;
    }

    private String getOrderBy(Page page){
        if(page.getOrderBy() != null && page.getColumnName() != null){
            return WebSortType.getValue(page.getColumnName()) + " " + page.getOrderBy();
        }
        return null;
    }

    @Override
    public SelectedDetail getArticleDetail(String articleId) {
        return null;
    }

    @Override
    public String updateArticle(String articleId, SelectedArticleDTO selectedArticleDTO)
        throws IllegalAccessException, InvocationTargetException {
        Article article = articleMapper.getArticleByArticleId(articleId);
        if (article == null) {
            return "未找到该文章";
        }
        if (!article.getIfStick() && selectedArticleDTO.getIfStick() != null && selectedArticleDTO.getIfStick()) {
            article.setStickUpdateTime(new Date());
        }
        Article newArticle = beanMapper.map(selectedArticleDTO, Article.class);
        newArticle.setTArticleId(article.getTArticleId());
        articleMapper.updateByPrimaryKeySelective(newArticle);

        if (selectedArticleDTO.getArticleRelatedEntityColumnDTOList() != null) {
            Example example = new Example(ArticleRelatedEntity.class);
            Criteria criteria = example.createCriteria();
            criteria.andEqualTo("articleId", articleId);
            articleRelatedEntityMapper.deleteByExample(example);
            for (ArticleRelatedEntityColumnDTO dto : selectedArticleDTO.getArticleRelatedEntityColumnDTOList()) {
                ArticleRelatedEntity articleRelatedEntity = new ArticleRelatedEntity();
                articleRelatedEntity.setArticleId(articleId);
                articleRelatedEntity.setEntityCode(dto.getEntityCode());
                articleRelatedEntity.setColumnType(dto.getColumnType());
                articleRelatedEntity.setOrderNum(ObjectUtils.firstNonNull(dto.getOrderNum(), 99));
                articleRelatedEntityMapper.insertSelective(articleRelatedEntity);
            }
        }

        ArticleContent articleContent = beanMapper.map(selectedArticleDTO, ArticleContent.class);
        articleContent.setArticleId(articleId);
        searchFromESService.updateArticle(articleContent);
        articleReloadService.reload(article);
        article = articleMapper.getArticleByArticleId(articleId);
        if (articleForAppService.checkIfSaveInRedis(article)) {
            articleBackUpService.backUpArticleToRedis(article);
        }
        return "更新成功";
    }

    @Override
    public EntityStat getEntityStat(String articleId, String title, Date date) {
        return null;
    }

    @Override
    public InputStream exportExcel(Date dateFrom, Date dateTo, String title) {
        List<String> titleIds = Collections.emptyList();
        if (StringUtils.isNotBlank(title)) {
            titleIds = searchFromESService.searchByTitle(title);
            if (CollectionUtils.isEmpty(titleIds)) {
                return excelService.writeExcel(new ArrayList<AllArticlePreview>(), dateFrom, dateTo,
                    cmuopEntityService.getCmuopEntityMap());
            }
        }
        List<SelectedPreview> selectedList =
            articleMapper.getSelectedViaJoin(dateFrom, dateTo, CollectionUtils.isEmpty(titleIds) ? null : titleIds,
                null);
        processor.processPreview(selectedList);
        return excelService.writeExcel(selectedList, dateFrom, dateTo, cmuopEntityService.getCmuopEntityMap());
    }
}
