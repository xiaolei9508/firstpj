package com.cmft.slas.cmuop.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author liurp001
 * @Since 2020/1/2
 */
@Data
@Accessors(chain = true)
public class EntityVO {
     private String entityCode;
     private String entityName;
}
