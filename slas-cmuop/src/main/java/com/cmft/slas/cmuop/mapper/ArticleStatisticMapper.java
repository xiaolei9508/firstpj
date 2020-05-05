package com.cmft.slas.cmuop.mapper;

import com.cmft.slas.cmuop.common.config.db.CommonMapper;
import com.cmft.slas.cmuop.dto.ArticleStatisticDTO;
import com.cmft.slas.cmuop.entity.ArticleStatistic;

import java.util.Date;
import java.util.List;

public interface ArticleStatisticMapper extends CommonMapper<ArticleStatistic>{

    long countByCondition(ArticleStatisticDTO articleStatisticDTO);

    List<ArticleStatistic> queryByCondition(ArticleStatisticDTO articleStatisticDTO);

    List<ArticleStatistic> queryArticleStatisticList();

    Date queryUpdateTimeByMaxTime();

}
