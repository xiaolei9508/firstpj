package com.cmft.slas.cmuop.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author liurp001
 * @Since 2020/1/9
 */
@Data
@Accessors(chain = true)
public class BlockingReasonsVO {
    @ApiModelProperty(value = "屏蔽原因字典编码")
    private String code;
    @ApiModelProperty(value = "屏蔽原因名")
    private String name;
}
