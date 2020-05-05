package com.cmft.slas.cmuop.dto;

import java.io.Serializable;
import java.util.List;

import com.cmft.slas.cmuop.common.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel(value = "CmuopEntityDTO", description = "实体")
@EqualsAndHashCode(callSuper = false)
public class CmuopEntityDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long entityId;

    /**
     * 组织名称
     */
    @ApiModelProperty(value = "组织名称")
    private String entityName;

    /**
     * 组织编码
     */
    @ApiModelProperty(value = "组织编码")
    private String entityCode;

    /**
     * 父级编码
     */
    @ApiModelProperty(value = "父级编码")
    private String parentCode;

    /**
     * 组织编码NUC
     */
    @ApiModelProperty(value = "组织编码NUC")
    private String entityCodeNuc;

    /**
     * 同义词
     */
    @ApiModelProperty(value = "同义词")
    private String searchWords;

    private List<String> searchWordList;

    /**
     * 所属行业板块id
     */
    @ApiModelProperty(value = "所属行业板块id")
    private String industryId;

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
    @ApiModelProperty(value = "是否生效")
    private Byte isValid;

    /**
     * 实体关系
     */
    @ApiModelProperty(value = "实体关系")
    private List<CmuopEntityDTO> relatedEntityList;
}
