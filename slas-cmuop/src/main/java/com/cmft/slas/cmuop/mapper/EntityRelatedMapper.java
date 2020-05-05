package com.cmft.slas.cmuop.mapper;

import java.util.List;

import com.cmft.slas.cmuop.common.config.db.CommonMapper;
import com.cmft.slas.cmuop.dto.EntityRelatedDTO;
import com.cmft.slas.cmuop.entity.EntityRelated;

public interface EntityRelatedMapper extends CommonMapper<EntityRelated> {

    long countByCondition(EntityRelatedDTO entityRelatedDTO);

    List<EntityRelated> queryByCondition(EntityRelatedDTO entityRelatedDTO);

    List<EntityRelated> queryEntityRelatedList();
}
