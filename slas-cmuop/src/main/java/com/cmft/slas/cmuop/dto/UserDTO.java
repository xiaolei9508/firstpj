package com.cmft.slas.cmuop.dto;

import java.io.Serializable;
import java.util.List;

import com.cmft.slas.cmuop.common.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel(value = "UserDTO", description = "用户信息表")
@EqualsAndHashCode(callSuper = false)
public class UserDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
    * 
    */
    private Long userId;

    /**
     * nuc uid
     */
    @ApiModelProperty(value = "nuc uid")
    private String uid;

    /**
     * ps Id
     */
    @ApiModelProperty(value = "ps Id")
    private String psId;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String name;

    /**
     * 所属公司
     */
    @ApiModelProperty(value = "所属公司")
    private String company;

    private String imgUrl;

    /**
     * 头像数据
     */
    private String imgBase64;

    /**
     * 是否可用
     */
    @ApiModelProperty(value = "是否可用")
    private Byte isValid;
    /**
     * 职位列表
     */
    @ApiModelProperty(value = "职位列表")
    private List<UserEntityDTO> entrepreneurList;

    private Integer orderNum;

}
