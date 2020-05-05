package com.cmft.slas.cmuop.mapper;

import java.util.List;

import com.cmft.slas.cmuop.common.config.db.CommonMapper;
import com.cmft.slas.cmuop.dto.CommentStatisticDTO;
import com.cmft.slas.cmuop.entity.CommentStatistic;

public interface CommentStatisticMapper extends CommonMapper<CommentStatistic>{
    
    long countByCondition(CommentStatisticDTO commentStatisticDTO);

    List<CommentStatistic> queryByCondition(CommentStatisticDTO commentStatisticDTO);

	List<CommentStatistic> queryCommentStatisticList();
}
