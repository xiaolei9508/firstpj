package com.cmft.slas.cmuop.dto;

import com.cmft.slas.cmuop.common.dto.BaseDTO;
import com.cmft.slas.cmuop.entity.CmuopEntityType;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@ApiModel(value = "CmuopEntityTypeDTO", description = "实体对应新闻栏目")
@EqualsAndHashCode(callSuper = false)
public class CmuopEntityTypeDTO extends BaseDTO {
    private Long entityId;
    private Integer orderNum;
    private String entityCode;
    private String entityName;
    private List<CmuopEntityTypeVO> entityTypeVOList;
}