package com.cmft.slas.cmuop.entity;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cmft.slas.cmuop.common.entity.BaseEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Table(name = "t_field")
@EqualsAndHashCode(callSuper = false)
@ToString
public class Field extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fieldId;

    /**
     * 行业名称
     * 
     */
    private String fieldName;

    /**
     * 行业编码
     * 
     */
    private String fieldCode;

    /**
     * 父级编码
     * 
     * 
     */
    private String parentCode;

    /**
     * 关键词
     * 
     */
    private String searchWords;

    /**
     *
     * 是否使用
     */
    @ApiModelProperty(value = "是否使用")
    private Byte isValid;
}
