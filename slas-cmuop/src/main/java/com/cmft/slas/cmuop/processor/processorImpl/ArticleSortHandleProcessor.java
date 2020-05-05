package com.cmft.slas.cmuop.processor.processorImpl;

import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.cmft.slas.cmuop.dto.ArticleForSortDTO;
import com.cmft.slas.cmuop.processor.ArticleSortProcessor;

@Component
public class ArticleSortHandleProcessor implements ArticleSortProcessor {
    @Override
    public void process(List<ArticleForSortDTO> articleList) {
        articleList.sort(new Comparator<ArticleForSortDTO>() {
            @Override
            public int compare(ArticleForSortDTO a1, ArticleForSortDTO a2) {
                if (a1.getIfAllStick() != null && a2.getIfAllStick() != null) {
                    if (!a1.getIfAllStick().equals(a2.getIfAllStick())) {
                        return a1.getIfAllStick() == 1 ? -1 : 1;
                    } else if (a1.getIfAllStick() == 1) {
                        return a1.getOrderNum() - a2.getOrderNum();
                    }
                }

                // 置顶文章处理
                /* 暂时取消已读沉底处理
                if (a1.getIfRead() != null && a2.getIfRead() != null) {
                    if (!a1.getIfRead().equals(a2.getIfRead())) {
                        return a1.getIfRead() ? 1 : -1;
                    }
                }
                */
                // if (a1.getIfRelatedEntity() != null && a2.getIfRelatedEntity() != null) {
                // if (!a1.getIfRelatedEntity().equals(a2.getIfRelatedEntity())) {
                // return a1.getIfRelatedEntity() ? -1 : 1;
                // }
                // }
                // if (a1.getIfIndustry() != null && a2.getIfIndustry() != null) {
                // if (!a1.getIfIndustry().equals(a2.getIfIndustry())) {
                // return a1.getIfIndustry() ? -1 : 1;
                // }
                // }
                // if (a1.getEntityMain() != null && a2.getEntityMain() != null) {
                // if (!a1.getEntityMain().equals(a2.getEntityMain())) {
                // return a2.getEntityMain().compareTo(a1.getEntityMain());
                // }
                // }
                if (a1.getTimeStep() != null && a2.getTimeStep() != null) {
                    if (!a1.getTimeStep().equals(a2.getTimeStep())) {
                        return a2.getTimeStep().compareTo(a1.getTimeStep());
                    }
                }

                if (a1.getEntityStep() != null && a2.getEntityStep() != null) {
                    if (!a1.getEntityStep().equals(a2.getEntityStep())) {
                        return a2.getEntityStep().compareTo(a1.getEntityStep());
                    }
                }

                if (!StringUtils.isEmpty(a1.getEntityLevel()) && !StringUtils.isEmpty(a2.getEntityLevel())) {
                    if (!a1.getEntityLevel().equals(a2.getEntityLevel())) {
                        return a2.getEntityLevel().compareTo(a1.getEntityLevel());
                    }
                }

                if (a1.getIfFromOa() != null && a2.getIfFromOa() != null) {
                    if (!a1.getIfFromOa().equals(a2.getIfFromOa())) {
                        return a1.getIfFromOa() ? -1 : 1;
                    }
                }

                if (a1.getIfEvent() != null && a2.getIfEvent() != null) {
                    if (!a1.getIfEvent().equals(a2.getIfEvent())) {
                        return a1.getIfEvent() ? -1 : 1;
                    }
                }
                if (a1.getIfManager() != null && a2.getIfManager() != null) {
                    if (!a1.getIfManager().equals(a2.getIfManager())) {
                        return a1.getIfManager() ? -1 : 1;
                    }
                }

                // if (StringUtils.isEmpty(a1.getImgUrl()) != StringUtils.isEmpty(a2.getImgUrl())) {
                // return StringUtils.isEmpty(a1.getImgUrl()) ? -1 : 1;
                // }
                // if (a1.getCreateTime() != null && a2.getCreateTime() != null) {
                // if (!a1.getCreateTime().equals(a2.getCreateTime())) {
                // return a1.getCreateTime().compareTo(a2.getCreateTime());
                // }
                // }
                return a2.getPubTime().compareTo(a1.getPubTime());

            }
        });
    }

    @Override
    public void process(List<ArticleForSortDTO> articleList, String uid) {
        process(articleList);
    }

    @Override
    public void process(List<ArticleForSortDTO> articleList, String uid, String columnType) {
    }

    @Override
    public void process(ArticleForSortDTO article) {

    }
}
