package com.cmft.slas.cmuop.dto;

import java.io.Serializable;
import java.util.List;

import com.cmft.slas.cmuop.common.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel(value = "FieldDTO", description = "行业板块")
@EqualsAndHashCode(callSuper = false)
public class FieldDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long fieldId;

    /**
     * 行业名称
     * 
     */
    @ApiModelProperty(value = "行业名称")
    private String fieldName;

    /**
     * 行业编码
     * 
     */
    @ApiModelProperty(value = "行业编码")
    private String fieldCode;

    /**
     * 父级编码
     * 
     */
    @ApiModelProperty(value = "父级编码")
    private String parentCode;

    /**
     * 关键词
     * 
     */
    private String searchWords;

    /**
     * 关键词结合
     * 
     */
    private List<String> searchWordList;

    /**
     * 
     * 是否使用
     */
    @ApiModelProperty(value = "是否使用")
    private Byte isValid;
}
