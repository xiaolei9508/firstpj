package com.cmft.slas.cmuop.dto;

import java.io.Serializable;

import com.cmft.slas.cmuop.common.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel(value = "RuleDTO", description = "文章可见规则")
@EqualsAndHashCode(callSuper = false)
public class RuleDTO extends BaseDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
    /**
    * 
    */
    private Long ruleId;
    
    /**
    * 左参数
    */
    @ApiModelProperty(value = "左参数")
    private String leftParam;
    
    /**
    * 左参数类型
    */
    @ApiModelProperty(value = "左参数类型")
    private Integer leftParamType;
    
    /**
    * 条件
    */
    @ApiModelProperty(value = "条件")
    private String ruleCondition;
    
    /**
    * 左参数
    */
    @ApiModelProperty(value = "左参数")
    private String rightParam;
    
    /**
    * 右参数类型
    */
    @ApiModelProperty(value = "右参数类型")
    private Integer rightParamType;
    
    /**
    * 所属板块id
    */
    @ApiModelProperty(value = "所属板块id")
    private String partId;
}
