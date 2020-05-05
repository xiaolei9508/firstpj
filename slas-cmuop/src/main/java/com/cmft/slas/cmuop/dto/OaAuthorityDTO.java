package com.cmft.slas.cmuop.dto;

import java.io.Serializable;

import com.cmft.slas.cmuop.common.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel(value = "OaAuthorityDTO", description = "权限")
@EqualsAndHashCode(callSuper = false)
public class OaAuthorityDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
    * 
    */
    private Long oaAuthorityId;

    /**
     * nuc uid
     */
    @ApiModelProperty(value = "nuc uid")
    private String uid;

    /**
    * 
    */
    @ApiModelProperty(value = "")
    private String psId;

    /**
     * oa账号
     */
    @ApiModelProperty(value = "oa账号")
    private String oaId;

    /**
     * 分类权限
     */
    @ApiModelProperty(value = "分类权限")
    private String authAreaId;

    /**
     * 公司权限
     */
    @ApiModelProperty(value = "公司权限")
    private String authReader;
}
