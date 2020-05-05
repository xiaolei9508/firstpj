package com.cmft.slas.cmuop.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.cmft.slas.cmuop.common.dto.BaseDTO;
import com.cmft.slas.cmuop.vo.InfoStatVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel(value = "ArticleDTO", description = "文章")
@EqualsAndHashCode(callSuper = false)
public class ArticleDTO extends BaseDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

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
     * 网址
     */
    @ApiModelProperty(value = "网址")
    private String url;


    /**
     * 情感句
     */
    @ApiModelProperty(value = "情感句")
    private String sentiSentences;


    /**
    * 展示情感句
    */
    @ApiModelProperty(value = "展示情感句")
    private String sentiSentencesShow;

    /**
     * 是否来源于oa系统
     */
    @ApiModelProperty(value = "是否来源于oa系统")
    private Boolean ifFromOa;



    /**
     * 文章类别
     */
    @ApiModelProperty(value = "文章类别")
    private String typeOfContent;

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
    /**
    * 顺序
    */
    @ApiModelProperty(value = "顺序")
    private Integer orderNum;

    @ApiModelProperty(value = "舆情主体")
    private List<InfoStatVO> relatedEntityList;

    @ApiModelProperty(value = "文章分类")
    private List<ArticleRelatedEntityColumnDTO> articleRelatedEntityColumnDTOList;

    @ApiModelProperty(value = "文章正文(Html)")
    private String contentHtml;

    @ApiModelProperty(value = "轮播置顶更新时间")
    private Date stickUpdateTime;

    private Boolean ifRecommend;

}
