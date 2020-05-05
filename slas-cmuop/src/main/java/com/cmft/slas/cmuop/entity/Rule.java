package com.cmft.slas.cmuop.entity;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cmft.slas.cmuop.common.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Table(name = "t_rule")
@EqualsAndHashCode(callSuper = false)
@ToString
public class Rule extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
        
    /**
    * 
    */
    @Id
  	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ruleId;
    
    /**
    * 左参数
    */
    private String leftParam;
    
    /**
    * 左参数类型
    */
    private Integer leftParamType;
    
    /**
    * 条件
    */
    private String ruleCondition;
    
    /**
    * 左参数
    */
    private String rightParam;
    
    /**
    * 右参数类型
    */
    private Integer rightParamType;
    
    /**
    * 所属板块id
    */
    private String partId;
}
