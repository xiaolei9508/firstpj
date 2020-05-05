package com.cmft.slas.cmuop.service;

import java.util.List;

import com.cmft.slas.cmuop.dto.EntityRelatedDTO;
import com.cmft.slas.common.pojo.Page;
import com.github.pagehelper.PageInfo;

public interface EntityRelatedService {

    PageInfo<EntityRelatedDTO> getEntityRelatedList(EntityRelatedDTO entityRelatedDTO, Page page);

    int addEntityRelated(EntityRelatedDTO entityRelatedDTO);

    Long countByCondition(EntityRelatedDTO entityRelatedDTO);

    int deleteEntityRelated(Long id);

    int updateEntityRelated(EntityRelatedDTO entityRelatedDTO);

    EntityRelatedDTO queryEntityRelatedById(Long id);

    List<EntityRelatedDTO> queryByCondition(EntityRelatedDTO entityRelatedDTO);
}
