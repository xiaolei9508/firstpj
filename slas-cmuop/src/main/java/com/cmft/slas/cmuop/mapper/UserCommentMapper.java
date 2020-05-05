package com.cmft.slas.cmuop.mapper;

import java.util.Date;
import java.util.List;

import com.cmft.slas.cmuop.common.config.db.CommonMapper;
import com.cmft.slas.cmuop.dto.UserCommentDTO;
import com.cmft.slas.cmuop.entity.UserComment;
import org.apache.ibatis.annotations.Param;

public interface UserCommentMapper extends CommonMapper<UserComment> {

    long countByCondition(UserCommentDTO userCommentDTO);

    List<UserComment> queryByCondition(UserCommentDTO userCommentDTO);

    List<UserComment> queryByArticle(@Param("previousFireTime") Date previousFireTime,
        @Param("fireTime") Date fireTime);
}
