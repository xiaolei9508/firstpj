package com.cmft.slas.cmuop.mapper;

import java.util.List;

import com.cmft.slas.cmuop.dto.FieldDTO;
import com.cmft.slas.cmuop.entity.Field;
import com.cmft.slas.common.mapper.CommonMapper;

public interface FieldMapper extends CommonMapper<Field> {

    long countByCondition(FieldDTO fieldDTO);

    List<Field> queryByCondition(FieldDTO fieldDTO);

    List<Field> queryFieldList();
}
