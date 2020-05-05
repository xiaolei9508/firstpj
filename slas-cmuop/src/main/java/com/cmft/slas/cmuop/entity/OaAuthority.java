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
@Table(name = "t_oa_authority")
@EqualsAndHashCode(callSuper = false)
@ToString
public class OaAuthority extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oaAuthorityId;

    /**
     * nuc uid
     */
    private String uid;

    /**
    * 
    */
    private String psId;

    /**
     * oa账号
     */
    private String oaId;

    /**
     * 分类权限
     */
    private String authAreaId;

    /**
     * 公司权限
     */
    private String authReader;
}
