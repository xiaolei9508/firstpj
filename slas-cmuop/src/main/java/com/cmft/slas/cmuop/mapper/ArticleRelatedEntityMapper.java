package com.cmft.slas.cmuop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmft.slas.cmuop.common.config.db.CommonMapper;
import com.cmft.slas.cmuop.dto.ArticleRelatedEntityDTO;
import com.cmft.slas.cmuop.dto.EntitySentimentCountDTO;
import com.cmft.slas.cmuop.entity.ArticleRelatedEntity;

public interface ArticleRelatedEntityMapper extends CommonMapper<ArticleRelatedEntity>{
    
    long countByCondition(ArticleRelatedEntityDTO articleRelatedEntityDTO);

    List<ArticleRelatedEntity> queryByCondition(ArticleRelatedEntityDTO articleRelatedEntityDTO);

    List<ArticleRelatedEntity> selectDistinctEntity(ArticleRelatedEntityDTO articleRelatedEntityDTO);
    
    List<EntitySentimentCountDTO> getEntityStat(@Param("entityCode") String entityCode, @Param("days") Integer days);
}
