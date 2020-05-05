package com.cmft.slas.cmuop.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author liurp001
 * @Since 2020/1/2
 */
@Data
@Accessors(chain = true)
public class ReplyVO {
    @ApiModelProperty(value = "评论id")
    private Long commentId;

    @ApiModelProperty(value = "回复的评论的发布用户名")
    private String replyName;// 回复的用户

    @ApiModelProperty(value = "回复的评论的id")
    private Long replyId;

    @ApiModelProperty(value = "评论正文")
    private String comment;// 评论正文

    @ApiModelProperty(value = "这条评论的用户名")
    private String name;

    @ApiModelProperty(value = "这条评论的用户ID")
    private String uid;
}
