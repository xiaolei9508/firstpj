package com.cmft.slas.cmuop.dto;

import com.cmft.slas.cmuop.vo.PointViewTypeVO;
import lombok.Data;

import java.util.List;

@Data
public class CmuopEntityTypeWithPointInfoTypeVO {
    private Long entityTypeId;
    private String entityCode;
    private String columnType;
    private String columnName;
    private Integer orderNum;
    private List<PointViewTypeVO> pointInfoTypeList;
}
