package com.cmft.slas.cmuop.service;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmft.slas.cmuop.dto.UserViewDTO;
import com.cmft.slas.cmuop.vo.UserViewVO;

public interface UserViewService {

    Long countByCondition(UserViewDTO userViewDTO);

    List<UserViewDTO> queryByCondition(UserViewDTO userViewDTO);

    List<UserViewDTO> queryByArticle(@Param("previousFireTime") Date previousFireTime,@Param("fireTime") Date fireTime);

    List<UserViewVO> getUserView(String uid, String day);

}
