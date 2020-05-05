package com.cmft.slas.cmuop.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SortDTO {
    //排序方式
    private String orderType;

    //排序字段
    private String orderField;

}
