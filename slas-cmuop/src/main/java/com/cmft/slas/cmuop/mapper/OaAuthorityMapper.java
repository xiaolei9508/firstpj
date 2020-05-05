package com.cmft.slas.cmuop.mapper;

import java.util.List;

import com.cmft.slas.cmuop.common.config.db.CommonMapper;
import com.cmft.slas.cmuop.dto.OaAuthorityDTO;
import com.cmft.slas.cmuop.entity.OaAuthority;

public interface OaAuthorityMapper extends CommonMapper<OaAuthority> {

    long countByCondition(OaAuthorityDTO oaAuthorityDTO);

    List<OaAuthority> queryByCondition(OaAuthorityDTO oaAuthorityDTO);

    long insertOaAuthorityBatch(List<OaAuthorityDTO> list);

    void updateByUid(String uid);
}
