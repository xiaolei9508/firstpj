package com.cmft.slas.cmuop.service;

import com.cmft.slas.cmuop.dto.CmuopEntityTypeDTO;
import com.cmft.slas.cmuop.dto.CmuopEntityTypeDTO;
import com.cmft.slas.cmuop.dto.CmuopEntityTypeVO;
import com.cmft.slas.cmuop.dto.CmuopEntityTypeWithPointInfoTypeVO;
import com.cmft.slas.cmuop.entity.CmuopEntityType;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface EntityTypeService {

    PageInfo<CmuopEntityTypeDTO> queryEntityTypePage(String entityName, Integer pageNum, Integer pageSize);

    int updateEntityType(CmuopEntityTypeDTO cmuopEntityTypeDTO);

    int deleteEntityType(CmuopEntityTypeDTO cmuopEntityTypeDTO);

    void initEntityTypeByEntityCode(String entityCode, boolean isZhaoWen);

    List<CmuopEntityTypeVO> queryEntityTypeByCode(String entityCode);

    List<CmuopEntityTypeWithPointInfoTypeVO> queryEntityTypeWithPointInfoTypeByCode(String entityCode);
}
