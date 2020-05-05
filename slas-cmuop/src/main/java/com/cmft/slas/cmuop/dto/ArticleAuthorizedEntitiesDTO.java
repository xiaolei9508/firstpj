package com.cmft.slas.cmuop.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.cmft.slas.cmuop.common.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel(value = "ArticleAuthorizedEntitiesDTO", description = "文章允许访问")
@EqualsAndHashCode(callSuper = false)
public class ArticleAuthorizedEntitiesDTO extends BaseDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
    /**
    * 主键
    */
    private Long articleViewId;
    
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
    * 分类权限
    */
    @ApiModelProperty(value = "分类权限")
    private String authAreaId;
}
