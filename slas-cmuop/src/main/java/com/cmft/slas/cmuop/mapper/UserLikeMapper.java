package com.cmft.slas.cmuop.mapper;

import com.cmft.slas.cmuop.common.config.db.CommonMapper;
import com.cmft.slas.cmuop.dto.UserLikeDTO;
import com.cmft.slas.cmuop.entity.UserLike;
import com.cmft.slas.cmuop.vo.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface UserLikeMapper extends CommonMapper<UserLike> {

    long countByCondition(UserLikeDTO userLikeDTO);

    List<UserLike> queryByCondition(UserLikeDTO userLikeDTO);

    List<UserLike> queryUserLikeList();

    List<UserLike> queryByArticle(@Param("previousFireTime") Date previousFireTime,@Param("fireTime") Date fireTime);

    Integer ifUserLiked(UserLikeDTO userLikeDTO);

    List<UserVO> getUserLikesForArticle(String articleId);
}
