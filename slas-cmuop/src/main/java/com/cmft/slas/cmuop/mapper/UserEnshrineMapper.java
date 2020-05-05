package com.cmft.slas.cmuop.mapper;

import java.util.List;

import com.cmft.slas.cmuop.common.config.db.CommonMapper;
import com.cmft.slas.cmuop.dto.UserEnshrineDTO;
import com.cmft.slas.cmuop.entity.UserEnshrine;

public interface UserEnshrineMapper extends CommonMapper<UserEnshrine>{
    
    long countByCondition(UserEnshrineDTO userEnshrineDTO);

    List<UserEnshrine> queryByCondition(UserEnshrineDTO userEnshrineDTO);

	List<UserEnshrine> queryUserEnshrineList();
}
