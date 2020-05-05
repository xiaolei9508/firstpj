package com.cmft.slas.cmuop.vo;

import java.util.Date;
import java.util.List;

import com.cmft.slas.cmuop.dto.ArticleRelatedEntityColumnDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author liurp001
 * @Since 2019/12/27
 */
@Data
@Accessors(chain = true)
public class ArticleDetail {

    @ApiModelProperty(value = "序号")
    private Integer orderNum;


    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 文章id
     */
    @ApiModelProperty(value = "文章id")
    private String articleId;

    /**
     * 网址
     */
    @ApiModelProperty(value = "网址")
    private String url;

    /**
     * 文章类别
     */
    @ApiModelProperty(value = "文章类别")
    private Integer typeOfContent;

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
     * 情感句
     */
    @ApiModelProperty(value = "情感句")
    private String sentiSentences;

    /**
     * 情感句
     */
    @ApiModelProperty(value = "情感句(编辑后)")
    private String sentiSentencesShow;

    @ApiModelProperty(value = "文章正文(Html)")
    private String contentHtml;

    @ApiModelProperty(value = "舆情主体")
    private List<InfoStatVO> relatedEntityList;

    @ApiModelProperty(value = "文章分类")
    private List<ArticleRelatedEntityColumnDTO> articleRelatedEntityColumnDTOList;

    @ApiModelProperty(value = "类型")
    private Integer mediaType;

    @ApiModelProperty(value = "推送时间")
    private Date updateTime;

    @ApiModelProperty(value = "发布时间")
    private Date pubTime;

    @ApiModelProperty(value = "是否推送")
    private Boolean ifShow;

    @ApiModelProperty(value = "缩略图")
    private String imgUrl;

    @ApiModelProperty(value = "轮播图")
    private String topicImgUrl;

    @ApiModelProperty(value = "文章摘要")
    private String summary;
}
