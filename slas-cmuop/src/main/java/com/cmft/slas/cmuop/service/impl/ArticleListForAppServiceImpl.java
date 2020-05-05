 package com.cmft.slas.cmuop.service.impl;

 import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cmft.slas.cmuop.entity.Article;
import com.cmft.slas.cmuop.mapper.ArticleMapper;
import com.cmft.slas.cmuop.processor.ArticleFilterProcessor;
import com.cmft.slas.cmuop.processor.ArticlePreviewProcessor;
import com.cmft.slas.cmuop.service.ArticleListForAppService;
import com.cmft.slas.cmuop.vo.AppArticlePreview;

@Service
public class ArticleListForAppServiceImpl implements ArticleListForAppService {
    @Value("${recommend.mNumber}")
    private String mNumber;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticlePreviewProcessor processors;

    @Autowired
    private ArticleFilterProcessor articleFilterProcessor;

    @Override
    public List<AppArticlePreview> getArticleListForCarousels(String uid) {
        List<Article> carouselList = articleMapper.selectArticleListForCarousels();
        
        carouselList.sort((o1, o2) -> {
            if (o1.getOrderNum().equals(o2.getOrderNum())) {
                return (int)(o1.getTArticleId() - o2.getTArticleId());
            } else {
                return o1.getOrderNum() - o2.getOrderNum();
            }
        });

        List<AppArticlePreview> res = new ArrayList<>();
        for (Article article : carouselList) {
            AppArticlePreview app = new AppArticlePreview();
            BeanUtils.copyProperties(article, app);
            res.add(app);
        }

        processors.processPreview(res);

        // remove oa filter for carousel temp
        // res = articleFilterProcessor.process(res, uid);

        return res;
    }
}
