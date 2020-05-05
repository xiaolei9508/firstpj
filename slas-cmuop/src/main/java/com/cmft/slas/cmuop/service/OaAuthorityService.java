/**
 */
package com.cmft.slas.cmuop.service;

import java.util.List;

import com.cmft.slas.cmuop.dto.OaAuthorityDTO;

public interface OaAuthorityService {

    /**
     * 根据主键id删除
     *
     * @param id
     * @return
     */
    int deleteOaAuthority(Long id);

    int addOaAuthority(String uid);

    List<OaAuthorityDTO> queryOaAuthority();

    void synNucDataFile();
}
