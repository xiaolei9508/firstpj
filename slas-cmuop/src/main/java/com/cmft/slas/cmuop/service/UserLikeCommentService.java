package com.cmft.slas.cmuop.service;

import com.cmft.slas.cmuop.dto.UserLikeCommentDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface UserLikeCommentService {

    Long countByCondition(UserLikeCommentDTO userLikeCommentDTO);

    List<UserLikeCommentDTO> queryByCondition(UserLikeCommentDTO userLikeCommentDTO);

    List<UserLikeCommentDTO> queryByComment(@Param("previousFireTime") Date previousFireTime,
        @Param("fireTime") Date fireTime);

    boolean ifUserLikeComment(UserLikeCommentDTO userLikeCommentDTO);

    int likeThisArticleComment(UserLikeCommentDTO userLikeCommentDTO);

}
