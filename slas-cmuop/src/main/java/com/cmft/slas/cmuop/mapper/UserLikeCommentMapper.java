package com.cmft.slas.cmuop.mapper;

import com.cmft.slas.cmuop.common.config.db.CommonMapper;
import com.cmft.slas.cmuop.dto.UserLikeCommentDTO;
import com.cmft.slas.cmuop.entity.UserLikeComment;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface UserLikeCommentMapper extends CommonMapper<UserLikeComment>{
    
    long countByCondition(UserLikeCommentDTO userLikeCommentDTO);

    List<UserLikeComment> queryByCondition(UserLikeCommentDTO userLikeCommentDTO);

	List<UserLikeComment> queryUserLikeCommentList();

	List<UserLikeComment> queryByComment(@Param("previousFireTime") Date previousFireTime,@Param("fireTime") Date fireTime);

    Integer ifUserLikeComment(UserLikeCommentDTO userLikeCommentDTO);

    List<UserLikeComment> getAllLikesForComment(@Param("commentId")Long commentId);
}
