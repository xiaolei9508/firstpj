package com.cmft.slas.cmuop.dto;

import java.io.Serializable;

import com.cmft.slas.cmuop.common.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel(value = "ArticleLabelDTO", description = "文章标签")
@EqualsAndHashCode(callSuper = false)
public class ArticleLabelDTO extends BaseDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
    /**
    * 主键
    */
    private Long articleLabelId;
    
    /**
    * 文章id\n
    */
    @ApiModelProperty(value = "文章id")
    private String articleId;
    
    /**
    * 标签
    */
    @ApiModelProperty(value = "标签")
    private String labelId;
    
    /**
    * 标签类型

    */
    @ApiModelProperty(value = "标签类型")
    private String labelType;
}
