package com.cmft.slas.cmuop.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PointViewTypeVO {
    private String infoType;
    private String infoTypeName;
    private Integer sort;
    private String imgUrl;
    private Integer total;
    private Boolean haveOutline;
    private byte isDelete;
    private Boolean ifShow;
}
