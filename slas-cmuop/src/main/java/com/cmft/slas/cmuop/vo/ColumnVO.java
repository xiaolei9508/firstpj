package com.cmft.slas.cmuop.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ColumnVO implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String columnName;

    private String columnType;
}
