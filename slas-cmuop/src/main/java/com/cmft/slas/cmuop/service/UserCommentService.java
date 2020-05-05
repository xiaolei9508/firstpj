package com.cmft.slas.cmuop.service;

import com.cmft.slas.cmuop.dto.UserCommentDTO;
import com.cmft.slas.cmuop.vo.CommentVO;
import com.cmft.slas.common.pojo.Page;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface UserCommentService {

    Long countByCondition(UserCommentDTO userCommentDTO);

    List<UserCommentDTO> queryByCondition(UserCommentDTO userCommentDTO);

    List<UserCommentDTO> queryByArticle(@Param("previousFireTime") Date previousFireTime,
        @Param("fireTime") Date fireTime);

    Long addUserComment(UserCommentDTO userCommentDTO);

    int deleteUserComment(Long commentId);

    PageInfo<CommentVO> getUserComments(Long articleId, String uid, Page page);

}
