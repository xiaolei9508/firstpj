package com.cmft.slas.cmuop.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author liurp001
 * @Since 2020/1/15
 */
@Data
@Accessors(chain = true)
public class LabelVO {

    private String name;

    private String code;

    private String type;
}
