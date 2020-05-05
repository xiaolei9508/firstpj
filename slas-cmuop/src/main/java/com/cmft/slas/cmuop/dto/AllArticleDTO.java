package com.cmft.slas.cmuop.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AllArticleDTO extends ArticleDTO {

    /**
     * 是否分类置顶
     */
    @ApiModelProperty(value = "是否分类置顶")
    private Boolean ifPartStick;

    /**
     * 是否全部置顶
     */
    @ApiModelProperty(value = "是否全部置顶")
    private Boolean ifAllStick;

    /**
     * 是否置顶到轮播
     */
    private Boolean ifStick;
}
