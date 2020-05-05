package com.cmft.slas.cmuop.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "ArticleRelatedEntityColumnDTO", description = "文章分类")
@EqualsAndHashCode(callSuper = false)
public class ArticleRelatedEntityColumnDTO {
    /**
     * 主键
     */
    private Long articleEntityId;

    /**
     * 文章id
     */
    @ApiModelProperty(value = "文章id")
    private String articleId;

    /**
     * 组织编码
     */
    @ApiModelProperty(value = "实体编码")
    private String entityCode;

    /**
     * 组织编码
     */
    @ApiModelProperty(value = "实体编码")
    private String entityName;

    /**
     * 所属分类
     */
    @ApiModelProperty(value = "所属分类")
    private String columnType;

    /**
     * 所属分类名称
     */
    @ApiModelProperty(value = "所属分类名称")
    private String columnName;

    /**
     * 序号
     */
    @ApiModelProperty(value = "序号")
    private Integer orderNum;
}
