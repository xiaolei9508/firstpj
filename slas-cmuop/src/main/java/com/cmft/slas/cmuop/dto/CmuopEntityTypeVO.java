package com.cmft.slas.cmuop.dto;

import lombok.Data;

@Data
public class CmuopEntityTypeVO {
    private Long entityTypeId;
    private String entityCode;
    private String columnType;
    private String columnName;
    private Integer orderNum;
}

