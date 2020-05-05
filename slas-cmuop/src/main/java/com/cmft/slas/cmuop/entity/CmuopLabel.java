package com.cmft.slas.cmuop.entity;

import java.io.Serializable;

import javax.persistence.Table;

import com.cmft.slas.cmuop.common.entity.BaseEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Table(name = "t_cmuop_label")
@EqualsAndHashCode(callSuper = false)
@ToString
public class CmuopLabel extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long labelId;

    /**
     * 标签名称
     *
     */
    @ApiModelProperty(value = "标签名称")
    private String labelName;

    /**
     * 标签编码
     *
     */
    @ApiModelProperty(value = "标签编码")
    private String labelCode;

    /**
     * 父级标签编码
     *
     */
    @ApiModelProperty(value = "父级标签编码")
    private String parentCode;

    /**
     * 标签类型
     */
    @ApiModelProperty(value = "标签类型")
    private String labelType;

    @ApiModelProperty(value = "搜索词")
    private String searchWords;

    @ApiModelProperty(value = "关注招商实体ID")
    private String belongedEntityId;

    @ApiModelProperty(value = "是否使用")
    private Byte isValid;
}
