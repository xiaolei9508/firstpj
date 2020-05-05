package com.cmft.slas.cmuop.mapper;

import com.cmft.slas.cmuop.common.config.db.CommonMapper;
import com.cmft.slas.cmuop.entity.PointInfoType;

public interface CmuopPointInfoTypeMapper extends CommonMapper<PointInfoType> {
    String getMaxInfoType();
}
