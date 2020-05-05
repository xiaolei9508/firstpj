package com.cmft.slas.cmuop.mapper;

import java.util.List;

import com.cmft.slas.cmuop.common.config.db.CommonMapper;
import com.cmft.slas.cmuop.dto.UserEntityDTO;
import com.cmft.slas.cmuop.entity.UserEntity;

public interface UserEntityMapper extends CommonMapper<UserEntity>{
    
    long countByCondition(UserEntityDTO userEntityDTO);

    List<UserEntity> queryByCondition(UserEntityDTO userEntityDTO);

	List<UserEntity> queryUserEntityList();
}
