package com.cmft.slas.cmuop.vo;

import com.cmft.slas.cmuop.common.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ArticleVO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文章id")
    private String articleId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容html")
    private String contentHtml;

    @ApiModelProperty(value="发布日期")
    private Date pubTime;

    @ApiModelProperty(value = "点赞统计")
    private Integer likeCount;

    @ApiModelProperty(value = "用户是否点过赞")
    private Boolean ifLike;

    @ApiModelProperty(value = "评论树")
    private List<CommentVO> comments;

    @ApiModelProperty(value = "图片地址")
    private String imgUrl;

    @ApiModelProperty(value = "原文地址")
    private String url;

    @ApiModelProperty(value = "源")
    private String source;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "点赞用户列表")
    private List<UserVO> likeUsers;

    @ApiModelProperty(value = "情感分类")
    private Integer sentiment;

    @ApiModelProperty(value = "标签")
    private List<LabelVO> labels;

    @ApiModelProperty(value = "评论总数")
    private Integer commentCount;
}
