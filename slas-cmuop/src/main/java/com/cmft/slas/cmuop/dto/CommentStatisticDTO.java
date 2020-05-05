package com.cmft.slas.cmuop.dto;

import java.io.Serializable;
import java.util.Date;

import com.cmft.slas.cmuop.common.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel(value = "CommentStatisticDTO", description = "评论统计")
@EqualsAndHashCode(callSuper = false)
public class CommentStatisticDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    private Long commentStatisticId;

    /**
     * 评论id
     */
    @ApiModelProperty(value = "评论id")
    private Long commentId;

    /**
     * 点赞数量
     */
    @ApiModelProperty(value = "点赞数量")
    private Integer likeNum;

    private Date createTimeRange;
    private Date updateTimeRange;
}
