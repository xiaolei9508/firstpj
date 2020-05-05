package com.cmft.slas.cmuop.dto;

import java.io.Serializable;

import com.cmft.slas.cmuop.common.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "ArticleRelatedEntityDTO", description = "文章相关")
@EqualsAndHashCode(callSuper = false)
public class ArticleRelatedEntityDTO extends BaseDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
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
    @ApiModelProperty(value = "组织编码")
    private String entityCode;

    /**
     * 所属分类
     */
    @ApiModelProperty(value = "所属分类")
    private String columnType;

    /**
     * 序号
     */
    @ApiModelProperty(value = "序号")
    private Integer orderNum;
}
