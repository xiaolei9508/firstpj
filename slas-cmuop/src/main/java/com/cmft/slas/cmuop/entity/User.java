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
@Table(name = "t_user")
@EqualsAndHashCode(callSuper = false)
@ToString
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    /**
     * nuc uid
     */
    private String uid;

    /**
     * ps Id
     */
    private String psId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 所属公司
     */
    private String company;

    /**
     * 是否可用
     */
    @ApiModelProperty(value = "是否可用")
    private Byte isValid;

    private String imgUrl;

}
