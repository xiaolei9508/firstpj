package com.cmft.slas.cmuop.vo;

import java.io.Serializable;

import com.cmft.slas.cmuop.common.dto.BaseDTO;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EntityTrendVO extends BaseDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String entityName;

    private String entityCode;

    private Integer total;

    private Integer positive;

    private Integer negative;
}
