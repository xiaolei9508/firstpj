package com.cmft.slas.cmuop.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmft.slas.cmuop.common.config.db.CommonMapper;
import com.cmft.slas.cmuop.dto.UserViewDTO;
import com.cmft.slas.cmuop.entity.UserLike;
import com.cmft.slas.cmuop.entity.UserView;
import com.cmft.slas.cmuop.vo.UserViewVO;

public interface UserViewMapper extends CommonMapper<UserView> {

    long countByCondition(UserViewDTO userViewDTO);

    List<UserView> queryByCondition(UserViewDTO userViewDTO);

    List<UserLike> queryByArticle(@Param("previousFireTime") Date previousFireTime, @Param("fireTime") Date fireTime);

    List<UserView> selectUserViewListByArticleIdList(@Param("list") List<String> list, @Param("uid") String uid);

    Integer countByDay(String from, String to);

    List<UserViewVO> getUserView(String uid, String from, String to);

    Boolean ifUserRead(@Param("uid") String uid, @Param("articleId") String articleId);
}
