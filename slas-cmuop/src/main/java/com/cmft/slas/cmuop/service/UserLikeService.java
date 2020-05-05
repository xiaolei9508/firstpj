package com.cmft.slas.cmuop.service;

import com.cmft.slas.cmuop.dto.UserLikeDTO;
import com.cmft.slas.cmuop.vo.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface UserLikeService {

    Long countByCondition(UserLikeDTO userLikeDTO);

    List<UserLikeDTO> queryByCondition(UserLikeDTO userLikeDTO);

    List<UserLikeDTO> queryByArticle(@Param("previousFireTime") Date previousFireTime,
        @Param("fireTime") Date fireTime);

    boolean ifUserLiked(UserLikeDTO userLikeDTO);

    int likeThisArticle(UserLikeDTO userLikeDTO);

    List<UserVO> getUserLikes(String articleId);
}
