package com.cmft.slas.cmuop.dto;

import com.cmft.slas.cmuop.common.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@ApiModel(value = "UserViewDTO", description = "用户浏览")
@EqualsAndHashCode(callSuper = false)
public class AddNumDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private Long userViewId;

    /**
     * 文章id\n
     */
    @ApiModelProperty(value = "文章id")
    private String articleId;

    /**
     * 增量
     */
    @ApiModelProperty(value = "增量")
    private Integer count;
}
