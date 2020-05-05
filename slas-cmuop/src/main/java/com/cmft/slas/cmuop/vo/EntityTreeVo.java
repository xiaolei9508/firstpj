package com.cmft.slas.cmuop.vo;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class EntityTreeVo {
    private String entityCode;
    private String entityName;
    private Integer orderNum;
    private String parentCode;
    private List<EntityTreeVo> childEntity;
}
