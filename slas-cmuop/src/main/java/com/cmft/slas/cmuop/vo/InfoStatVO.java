package com.cmft.slas.cmuop.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author liurp001
 * @Since 2019/12/30
 */
@Data
@Accessors(chain = true)
public class InfoStatVO implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String name;
    private String code;
    private Integer count;
}
