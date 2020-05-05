package com.cmft.slas.cmuop.dto;

import java.io.Serializable;
import java.util.List;

import com.cmft.slas.cmuop.common.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel(value = "CmuopLabelDTO", description = "标签")
@EqualsAndHashCode(callSuper = false)
public class CmuopLabelDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long labelId;

    /**
     * 标签名称
     * 
     */
    @ApiModelProperty(value = "标签名称")
    private String labelName;

    /**
     * 标签编码
     * 
     */
    @ApiModelProperty(value = "标签编码")
    private String labelCode;

    /**
     * 父级标签编码
     * 
     */
    @ApiModelProperty(value = "父级标签编码")
    private String parentCode;

    /**
     * 标签类型
     */
    @ApiModelProperty(value = "标签类型")
    private String labelType;

    @ApiModelProperty(value = "搜索词")
    private String searchWords;

    private List<String> searchWordList;

    @ApiModelProperty(value = "关注招商实体ID")
    private String belongedEntityId;

    @ApiModelProperty(value = "关注招商实体名称")
    private String belongedEntityName;

    @ApiModelProperty(value = "是否使用")
    private Byte isValid;
}
