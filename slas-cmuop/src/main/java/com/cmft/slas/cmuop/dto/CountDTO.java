package com.cmft.slas.cmuop.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel(value = "EntityRelatedDTO", description = "实体相关")
@EqualsAndHashCode(callSuper = false)
public class CountDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;

    private Integer count;
}
