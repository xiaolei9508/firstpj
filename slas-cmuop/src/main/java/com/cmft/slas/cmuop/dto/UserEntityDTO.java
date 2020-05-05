package com.cmft.slas.cmuop.dto;

import java.io.Serializable;

import com.cmft.slas.cmuop.common.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel(value = "UserEntityDTO", description = "用户可见组织")
@EqualsAndHashCode(callSuper = false)
public class UserEntityDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    private Long userEntityId;

    /**
     * nuc uid
     */
    @ApiModelProperty(value = "nuc uid")
    private String uid;

    /**
     * 组织编码
     */
    @ApiModelProperty(value = "组织编码")
    private String entityCode;

    /**
     * 组织名称
     */
    @ApiModelProperty(value = "组织名称")
    private String entityName;

    /**
     * 职位
     */
    @ApiModelProperty(value = "职位")
    private String entrepreneurPosition;

    /**
     * 是否来自nuc
     */
    @ApiModelProperty(value = "是否来自nuc")
    private Byte fromNuc;

    /**
     * 是否生效
     */
    @ApiModelProperty(value = "是否生效")
    private Byte isValid;
}
