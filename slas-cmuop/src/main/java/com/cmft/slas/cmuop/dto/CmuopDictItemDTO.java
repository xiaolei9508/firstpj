package com.cmft.slas.cmuop.dto;

import java.io.Serializable;

import com.cmft.slas.cmuop.common.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel(value = "CmuopDictItemDTO", description = "数据字典")
@EqualsAndHashCode(callSuper = false)
public class CmuopDictItemDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    private Long tDictItemId;

    /**
     * 字典编码
     */
    @ApiModelProperty(value = "字典编码")
    private String dictCode;

    /**
     * 字典名
     */
    private String dictName;

    /**
     * 字典项值
     */
    @ApiModelProperty(value = "字典项值")
    private String value;

    /**
     * 字典项文本
     */
    @ApiModelProperty(value = "字典项文本")
    private String text;

    /**
     * 字典项英文文本，下划线分割
     */
    @ApiModelProperty(value = "字典项英文文本，下划线分割")
    private String engText;

    /**
     * 字典项序号
     */
    @ApiModelProperty(value = "字典项序号")
    private Byte sort;
}
