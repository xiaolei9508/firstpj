package com.cmft.slas.cmuop.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CommentVO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 该评论的Id
     */
    @ApiModelProperty(value = "评论id")
    private Long commentId;


    /**
     * 评论正文
     */
    @ApiModelProperty(value = "评论正文")
    private String comment;// 评论正文

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;// 创建时间

    /*
     * 发布人id
     */
    @ApiModelProperty(value = "发布人id")
    private String uid;// 发布人id

    /**
     * 评论点赞数
     */
    @ApiModelProperty(value = "该评论的点赞数")
    private Integer likeCount;// 该评论的点赞数


    /**
     * 子评论列表
     */
    @ApiModelProperty(value = "子评论列表")
    private List<ReplyVO> children;// 子评论列表

    /*
     * 用户是否喜欢
     */
    @ApiModelProperty(value = "用户是否喜欢")
    private Boolean ifLike;

    /*
     * 用户
     */
    @ApiModelProperty(value = "用户")
    private UserVO user;

}
