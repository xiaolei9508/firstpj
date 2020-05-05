package com.cmft.slas.cmuop.vo;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author liurp001
 * @Since 2019/12/27
 */
@Data
@Accessors(chain = true)
public class EntityStat {

    private List<InfoStatVO> ifShowStatList;

    private List<InfoStatVO> entityStatList;

    private List<InfoStatVO> thirdLevelStatList;

    private List<InfoStatVO> sentimentStatList;

}
