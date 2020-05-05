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
@Table(name = "t_cmuop_dict_item")
@EqualsAndHashCode(callSuper = false)
@ToString
public class CmuopDictItem extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tDictItemId;

    /**
     * 字典编码
     */
    private String dictCode;

    /**
     * 字典名
     */
    private String dictName;

    /**
     * 字典项值
     */
    private String value;

    /**
     * 字典项文本
     */
    private String text;

    /**
     * 字典项英文文本，下划线分割
     */
    private String engText;

    /**
     * 字典项序号
     */
    private Byte sort;
}
