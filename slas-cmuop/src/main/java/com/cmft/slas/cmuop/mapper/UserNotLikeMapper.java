package com.cmft.slas.cmuop.mapper;

import java.util.List;

import com.cmft.slas.cmuop.common.config.db.CommonMapper;
import com.cmft.slas.cmuop.dto.UserNotLikeDTO;
import com.cmft.slas.cmuop.entity.UserNotLike;

public interface UserNotLikeMapper extends CommonMapper<UserNotLike>{
    
    long countByCondition(UserNotLikeDTO userNotLikeDTO);

    List<UserNotLike> queryByCondition(UserNotLikeDTO userNotLikeDTO);

	List<UserNotLike> queryUserNotLikeList();

    List<String> selectUserNotLike(String uid);
}
