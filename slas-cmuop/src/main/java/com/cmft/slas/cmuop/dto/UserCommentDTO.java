package com.cmft.slas.cmuop.dto;

import com.cmft.slas.cmuop.common.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@ApiModel(value = "UserCommentDTO", description = "用户评论")
@EqualsAndHashCode(callSuper = false)
public class UserCommentDTO extends BaseDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
    /**
    * 主键id
    */
    private Long commentId;

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
    * 
    */
    @ApiModelProperty(value = "")
    private String comment;
    
    /**
    * 回复评论id
    */
    @ApiModelProperty(value = "回复评论id")
    private Long replyId;

    @ApiModelProperty(value = "回复评论的用户的名字")
    private String replyName;

    @ApiModelProperty(value = "增量")
    private Integer count;

    @ApiModelProperty(value = "根评论id")
    private Long rootId;
}
