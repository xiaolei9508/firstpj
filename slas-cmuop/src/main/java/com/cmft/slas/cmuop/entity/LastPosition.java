package com.cmft.slas.cmuop.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LastPosition implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String entityCode;

    private Long offset;

    private Boolean runOut;

    /*
        used for LEADER column type
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String pubTime;

    private Integer orderNum;
}
