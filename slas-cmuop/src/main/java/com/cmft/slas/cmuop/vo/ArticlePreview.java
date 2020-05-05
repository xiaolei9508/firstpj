package com.cmft.slas.cmuop.vo;


import com.cmft.slas.cmuop.common.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @Author liurp001
 * @Since 2019/12/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ArticlePreview extends BaseDTO {
    /**
     * 文章id
     */
    @ApiModelProperty(value = "文章id")
    private String articleId;

    /**
     * 媒体类型
     */
    @ApiModelProperty(value = "媒体类型")
    private Integer mediaType;

    /**
     * 发布日期
     */
    @ApiModelProperty(value = "发布日期")
    private Date pubDate;

    /**
     * 发布时间
     */
    @ApiModelProperty(value = "发布时间")
    private Date pubTime;

    /**
     * 作者
     */
    @ApiModelProperty(value = "作者")
    private String author;

    /**
     * 发布来源
     */
    @ApiModelProperty(value = "发布来源")
    private String source;

    /**
     * 情感分类
     */
    @ApiModelProperty(value = "情感分类")
    private Integer sentiment;


    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 文章类别
     */
    @ApiModelProperty(value = "文章类别")
    private List<String> typeOfContent;

    /**
     * 是否显示
     */
    @ApiModelProperty(value = "是否显示")
    private Boolean ifShow;

    /**
     *
     */
    @ApiModelProperty(value = "缩略图url")
    private String imgUrl;

    @ApiModelProperty(value = "轮播图url")
    private String topicImgUrl;

    @ApiModelProperty(value = "浏览量")
    private Integer viewCount;

    @ApiModelProperty(value = "舆情主题列表")
    private List<String> entities;

    @ApiModelProperty(value = "原文链接")
    private String url;

    @ApiModelProperty(value = "信任等级")
    private Integer trustLevel;
}
