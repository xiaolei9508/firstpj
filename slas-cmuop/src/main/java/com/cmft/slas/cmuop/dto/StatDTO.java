package com.cmft.slas.cmuop.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author liurp001
 * @Since 2019/12/30
 */
@Data
@Accessors(chain = true)
public class StatDTO {
    List<Boolean> ifShowStatList;

    List<String> entityStatList;

    List<String> thirdLevelStatList;

    List<Integer> sentimentStatList;
}
