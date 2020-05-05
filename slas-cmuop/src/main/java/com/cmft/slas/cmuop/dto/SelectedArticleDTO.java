package com.cmft.slas.cmuop.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SelectedArticleDTO extends ArticleDTO {

    @ApiModelProperty(value = "是否置顶轮播")
    private Boolean ifStick;
}
