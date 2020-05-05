package com.cmft.slas.cmuop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmft.slas.cmuop.common.config.db.CommonMapper;
import com.cmft.slas.cmuop.dto.UserDTO;
import com.cmft.slas.cmuop.entity.User;

public interface UserMapper extends CommonMapper<User> {

    long countByCondition(UserDTO userDTO);

    List<User> queryByCondition(UserDTO userDTO);

    List<User> queryUserList();

    List<String> queryUserUidList();

    Integer countByDay(String from, String to);

    List<User> queryUserInfoByUserId(@Param("userId") String userId);

    List<User> queryUserInfo();
}
