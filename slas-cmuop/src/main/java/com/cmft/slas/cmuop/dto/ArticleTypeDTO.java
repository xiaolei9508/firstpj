package com.cmft.slas.cmuop.dto;

import java.io.Serializable;

import com.cmft.slas.cmuop.common.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel(value = "ArticleTypeDTO", description = "文章类型")
@EqualsAndHashCode(callSuper = false)
public class ArticleTypeDTO extends BaseDTO implements Serializable {
	
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
    * 类型id
    */
    @ApiModelProperty(value = "类型id")
    private String typeId;
}
