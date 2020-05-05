package com.cmft.slas.cmuop.dto;

import java.io.Serializable;

import com.cmft.slas.cmuop.common.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel(value = "UserLikeDTO", description = "用户点赞文章")
@EqualsAndHashCode(callSuper = false)
public class UserLikeDTO extends BaseDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
    /**
    * 
    */
    private Long userLikeId;
    
    /**
    * 文章id

    */
    @ApiModelProperty(value = "文章id")
    private String articleId;
    
    /**
    * nuc uid
    */
    @ApiModelProperty(value = "nuc uid")
    private String uid;
    
	/**
    * 操作类型
    */
    @ApiModelProperty(value = "操作类型")
    private Byte type;
}
