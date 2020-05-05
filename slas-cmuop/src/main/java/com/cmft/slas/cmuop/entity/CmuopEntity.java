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
import lombok.experimental.Accessors;

@Data
@Table(name = "t_cmuop_entity")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ToString
public class CmuopEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long entityId;
    /**
     * 组织名称
     */
    private String entityName;

    /**
     * 组织编码
     */
    private String entityCode;

    /**
     * 父级编码
     */
    private String parentCode;

    /**
     * 组织编码NUC
     */
    private String entityCodeNuc;

    /**
     * 同义词
     */
    private String searchWords;

    /**
     * 所属行业板块id
     */
    private String industryId;

    private Integer orderNum;

    /**
     * 是否来自nuc
     * 
     */
    private Byte fromNuc;

    /**
     * 是否在招商随行中使用
     * 
     */
    private Byte ifCmuop;

    /**
     * 是否生效
     */
    private Byte isValid;

}
