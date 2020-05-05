package com.cmft.slas.cmuop.service;

import com.cmft.slas.cmuop.dto.ArticleRelatedEntityDTO;

public interface ArticleRelatedEntityService {
    Long countByCondition(ArticleRelatedEntityDTO articleRelatedEntityDTO);

    void batchProcessEntityWithRecommend(String articleId, Boolean ifRecommend);

    boolean checkIfEntityExists(String articleId);

    void processAllEntityWithRecommend(String articleId, Boolean ifRecommend);
}
