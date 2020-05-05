package com.cmft.slas.cmuop.entity;

import java.io.Serializable;

import javax.persistence.*;

import com.cmft.slas.cmuop.common.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Table(name = "t_user_entity")
@EqualsAndHashCode(callSuper = false)
@ToString
@Accessors(chain = true)
public class UserEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userEntityId;

    /**
     * nuc uid
     */
    private String uid;

    /**
     * 组织编码
     */
    private String entityCode;

    /**
     * 职位
     */
    private String entrepreneurPosition;

    /**
     * 是否来自nuc
     */
    private Byte fromNuc;

    /**
     * 是否生效
     */
    private Byte isValid;

    private Integer orderNum;
}
