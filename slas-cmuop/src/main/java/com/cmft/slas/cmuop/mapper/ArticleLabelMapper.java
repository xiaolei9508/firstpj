package com.cmft.slas.cmuop.mapper;

import java.util.List;

import com.cmft.slas.cmuop.common.config.db.CommonMapper;
import com.cmft.slas.cmuop.dto.ArticleLabelDTO;
import com.cmft.slas.cmuop.entity.ArticleLabel;

public interface ArticleLabelMapper extends CommonMapper<ArticleLabel>{
    
    List<ArticleLabel> queryByCondition(ArticleLabelDTO articleLabelDTO);

	List<ArticleLabel> queryArticleLabelList();
}
