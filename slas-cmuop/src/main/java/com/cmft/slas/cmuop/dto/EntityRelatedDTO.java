package com.cmft.slas.cmuop.dto;

import java.io.Serializable;

import com.cmft.slas.cmuop.common.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel(value = "EntityRelatedDTO", description = "实体相关")
@EqualsAndHashCode(callSuper = false)

public class EntityRelatedDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    private Long entityRelatedId;

    /**
     * 实体id
     */
    @ApiModelProperty(value = "实体id")
    private String entityCode;

    @ApiModelProperty(value = "实体名称")
    private String entityName;

    /**
     * 关注实体id
     */
    @ApiModelProperty(value = "关注实体id")
    private String relatedEntityCode;

    /**
     * 关注实体名称
     */
    @ApiModelProperty(value = "关注实体名称")
    private String relatedEntityName;

    /**
     * 是否生效
     */
    @ApiModelProperty(value = "是否生效")
    private Byte isValid;
}
