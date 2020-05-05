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
@Table(name = "t_entity_related")
@EqualsAndHashCode(callSuper = false)
@ToString
public class EntityRelated extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long entityRelatedId;

    /**
     * 实体id
     */
    private String entityCode;

    /**
     * 关注实体id
     */
    private String relatedEntityCode;

    /**
     * 是否生效
     */
    private Byte isValid;
}
