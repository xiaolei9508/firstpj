package com.cmft.slas.cmuop.service;

import com.cmft.slas.cmuop.entity.CommentStatistic;

public interface CommentStatisticService {

    CommentStatistic selectOne(CommentStatistic commentStatistic);

    int updateCommentStatistic(CommentStatistic commentStatistic);

    int addCommentStatistic(CommentStatistic commentStatistic);

}
