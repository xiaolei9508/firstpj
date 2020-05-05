package com.cmft.slas.cmuop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmft.slas.cmuop.common.utils.BeanMapper;
import com.cmft.slas.cmuop.entity.CommentStatistic;
import com.cmft.slas.cmuop.mapper.CommentStatisticMapper;
import com.cmft.slas.cmuop.service.CommentStatisticService;

@Service("commentStatisticService")
public class CommentStatisticServiceImpl implements CommentStatisticService {
    @Autowired
    private CommentStatisticMapper commentStatisticMapper;
    @Autowired
    private BeanMapper beanMapper;

    @Override
    public CommentStatistic selectOne(CommentStatistic commentStatistic) {
        return commentStatisticMapper.selectOne(commentStatistic);
    }

    @Override
    public int updateCommentStatistic(CommentStatistic commentStatistic) {
        return commentStatisticMapper.updateByPrimaryKeySelective(beanMapper.map(commentStatistic, CommentStatistic.class));
    }

    @Override
    public int addCommentStatistic(CommentStatistic commentStatistic) {
        return commentStatisticMapper.insertSelective(beanMapper.map(commentStatistic, CommentStatistic.class));
    }
}
