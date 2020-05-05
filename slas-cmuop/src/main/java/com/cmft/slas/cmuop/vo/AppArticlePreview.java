package com.cmft.slas.cmuop.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AppArticlePreview extends ArticlePreview {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long tArticleId;

    /**
     * 实体名称
     */
    @ApiModelProperty(value = "实体名称")
    private String entityName;

    /**
     * 实体代码
     */
    @ApiModelProperty(value = "实体代码")
    private String entityCode;

    private Boolean ifFromOa;

    private Byte ifAllStick;

    private Byte ifPartStick;

    private Boolean ifRead;

}
