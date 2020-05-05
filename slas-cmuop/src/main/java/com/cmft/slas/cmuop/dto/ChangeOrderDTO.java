package com.cmft.slas.cmuop.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel(value = "ChangeOrderDTO", description = "排序修改")
@EqualsAndHashCode(callSuper = false)
public class ChangeOrderDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "序号")
    private Integer orderNum;

    @ApiModelProperty(value = "实体")
    private String entityCode;

    @ApiModelProperty(value = "分类")
    private String type;
}
