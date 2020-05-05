package com.cmft.slas.cmuop.mapper;

import java.util.List;

import com.cmft.slas.cmuop.common.config.db.CommonMapper;
import com.cmft.slas.cmuop.dto.CmuopDictItemDTO;
import com.cmft.slas.cmuop.entity.CmuopDictItem;

public interface CmuopDictItemMapper extends CommonMapper<CmuopDictItem> {

    long countByCondition(CmuopDictItemDTO cmuopDictItemDTO);

    List<CmuopDictItem> queryByCondition(CmuopDictItemDTO cmuopDictItemDTO);

    List<CmuopDictItem> queryCmuopDictItemList();
}
