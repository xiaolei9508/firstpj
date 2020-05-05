package com.cmft.slas.cmuop.mapper;

import java.util.List;

import com.cmft.slas.cmuop.common.config.db.CommonMapper;
import com.cmft.slas.cmuop.dto.ArticleTypeDTO;
import com.cmft.slas.cmuop.entity.ArticleType;

/**
 * @Author liurp001
 * @Since 2020/1/4
 */
public interface ArticleTypeMapper extends CommonMapper<ArticleType> {

    long countByCondition(ArticleTypeDTO articleTypeDTO);

    List<ArticleType> queryByCondition(ArticleTypeDTO articleTypeDTO);

    List<ArticleType> queryArticleTypeList();
}
