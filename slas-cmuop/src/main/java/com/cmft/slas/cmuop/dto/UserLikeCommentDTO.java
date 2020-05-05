package com.cmft.slas.cmuop.dto;

import java.io.Serializable;

import com.cmft.slas.cmuop.common.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel(value = "UserLikeCommentDTO", description = "用户点赞评论")
@EqualsAndHashCode(callSuper = false)
public class UserLikeCommentDTO extends BaseDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
    /**
    * 主键id
    */
    private Long userLikeId;
    
    /**
    * 文章id
    */
    @ApiModelProperty(value = "文章id")
    private String articleId;

    /**
     * 评论id
     */
    @ApiModelProperty(value = "评论id")
    private Long commentId;

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
