package com.cmft.slas.cmuop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmft.slas.cmuop.dto.CmuopLabelDTO;
import com.cmft.slas.cmuop.entity.CmuopLabel;
import com.cmft.slas.common.mapper.CommonMapper;

public interface CmuopLabelMapper extends CommonMapper<CmuopLabel> {

    long countByCondition(CmuopLabelDTO cmuopLabelDTO);

    List<CmuopLabel> queryByCondition(CmuopLabelDTO cmuopLabelDTO);

    int updateSelective(CmuopLabelDTO cmuopLabelDTO);

    CmuopLabel queryCmuopLabelById(@Param("id") Long id);
}
