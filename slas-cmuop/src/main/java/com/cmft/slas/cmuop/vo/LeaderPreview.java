package com.cmft.slas.cmuop.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LeaderPreview {
    private String uid;

    private String entityCode;

    private String userName;

    private Integer orderNum;
}
