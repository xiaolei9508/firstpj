package com.cmft.slas.cmuop.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.cmft.slas.cmuop.common.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel(value = "ArticleStatisticDTO", description = "文章统计")
@EqualsAndHashCode(callSuper = false)
public class ArticleStatisticDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    private Long articleStatisticId;

    /**
     * 文章id
     */
    @ApiModelProperty(value = "文章id")
    private String articleId;

    /**
     * 点赞数量
     */
    @ApiModelProperty(value = "点赞数量")
    private Integer likeNum;

    /**
     * 收藏数量
     */
    @ApiModelProperty(value = "收藏数量")
    private Integer enshrineNum;

    /**
     * 评论数
     */
    @ApiModelProperty(value = "评论数")
    private Integer commentNum;

    /**
     * 浏览量
     */
    @ApiModelProperty(value = "浏览量")
    private Integer viewNum;

    @ApiModelProperty(value = "最新更新时间")
    private Date maxTime;

    private Date createTimeRange;
    private Date updateTimeRange;
}
