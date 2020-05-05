package com.cmft.slas.cmuop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmft.slas.cmuop.dto.CmuopEntityDTO;
import com.cmft.slas.cmuop.entity.CmuopEntity;
import com.cmft.slas.common.mapper.CommonMapper;

public interface CmuopEntityMapper extends CommonMapper<CmuopEntity> {

    long countByCondition(CmuopEntityDTO cmuopEntityDTO);

    List<CmuopEntity> queryByCondition(CmuopEntityDTO cmuopEntityDTO);

    List<CmuopEntity> queryCmuopEntityList(CmuopEntityDTO cmuopEntityDTO);

    String queryEntityNameByEntityCode(@Param("entityCode") String entityCode);
}
