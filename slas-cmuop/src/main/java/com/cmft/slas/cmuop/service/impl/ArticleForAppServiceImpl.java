package com.cmft.slas.cmuop.service.impl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmft.slas.cmuop.entity.Article;
import com.cmft.slas.cmuop.entity.UserView;
import com.cmft.slas.cmuop.mapper.ArticleMapper;
import com.cmft.slas.cmuop.mapper.UserViewMapper;
import com.cmft.slas.cmuop.processor.ArticleProcessor;
import com.cmft.slas.cmuop.service.ArticleForAppService;
import com.cmft.slas.cmuop.vo.ArticleVO;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Service
public class ArticleForAppServiceImpl implements ArticleForAppService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleProcessor articleProcessor;

    @Autowired
    private UserViewMapper userViewMapper;


    @Override
    public ArticleVO getArticleDetail(String articleId, String uid) {
        long startTime = System.currentTimeMillis();
        Article article = articleMapper.selectOne(new Article().setArticleId(articleId));
        ArticleVO articleDetail = new ArticleVO();
        if (article == null){
            return articleDetail;
        }
        BeanUtils.copyProperties(article, articleDetail);
        articleProcessor.processVO(articleDetail, uid);

        try{
            // user view record
            UserView userView = new UserView();
            userView.setArticleId(articleId);
            userView.setUid(uid);
            userViewMapper.insertSelective(userView);
        }catch (Exception e){
            log.error("阅读行为记录错误："+ExceptionUtils.getStackTrace(e));
        }
        log.info("getArticleDetail cost:[{}]ms,title:{},uid:{},articleId:{}", System.currentTimeMillis()-startTime,articleDetail.getTitle(), uid, articleId);
        return articleDetail;
    }

    @Override
    public Boolean checkIfSaveInRedis(Article article) {
        if (article.getIfRecommend() && article.getIfShow() && !article.getIfAllStick()) {
            return true;
        }
        return false;
    }
}
