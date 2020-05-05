package com.cmft.slas.cmuop.service;

import com.cmft.slas.cmuop.vo.EntityTrendVO;
import com.cmft.slas.cmuop.vo.EntityVO;

import java.util.List;

/**
 * @Author liurp001
 * @Since 2020/1/2
 */
public interface EntityAppService {
    List<EntityVO> getAllEntities(String uid);

    List<EntityTrendVO> getEntityStatWRTUser(String uid, Integer dateBackNum);

    EntityTrendVO getEntityStat(String entityCode, Integer dateBackNum);

    List<String> getAllUserEntityCode(String uid);

    String reloadEntityList();
}
