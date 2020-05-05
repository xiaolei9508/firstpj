package com.cmft.slas.cmuop.mapper;

import java.util.List;

import com.cmft.slas.cmuop.common.config.db.CommonMapper;
import com.cmft.slas.cmuop.dto.ArticleAuthorizedEntitiesDTO;
import com.cmft.slas.cmuop.entity.ArticleAuthorizedEntities;

public interface ArticleAuthorizedEntitiesMapper extends CommonMapper<ArticleAuthorizedEntities>{
    
    long countByCondition(ArticleAuthorizedEntitiesDTO articleAuthorizedEntitiesDTO);

    List<ArticleAuthorizedEntities> queryByCondition(ArticleAuthorizedEntitiesDTO articleAuthorizedEntitiesDTO);

	List<ArticleAuthorizedEntities> queryArticleAuthorizedEntitiesList();
}
