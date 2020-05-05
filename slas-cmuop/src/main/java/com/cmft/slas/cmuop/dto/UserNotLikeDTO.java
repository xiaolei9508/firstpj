package com.cmft.slas.cmuop.dto;

import java.io.Serializable;

import com.cmft.slas.cmuop.common.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel(value = "UserNotLikeDTO", description = "用户拒绝")
@EqualsAndHashCode(callSuper = false)
public class UserNotLikeDTO extends BaseDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
    /**
    * 
    */
    private Long userNotLikeId;

    /**
    * 
    */
    @ApiModelProperty(value = "文章id")
    private String articleId;
    
    /**
    * 
    */
    @ApiModelProperty(value = "nuc用户id")
    private String uid;
    
    /**
    * 原因
    */
    @ApiModelProperty(value = "原因")
    private String reason;
}
