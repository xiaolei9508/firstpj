package com.cmft.slas.cmuop.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StatisticPerDayVO implements Serializable {
    private static final long serialVersionUID = 8165086531799929016L;
    /**
     * 当日同步数
     */
    private Integer synToday;

    /**
     * 当日同步失败数
     */
    private Integer synTodayFail;

    /**
     * 当日同步成功数
     */
    private Integer synTodaySuccess;

    /**
     * 历史总数
     */
    private Integer total;

    /**
     * 历史失败数
     */
    private Integer totalFail;

    /**
     * 历史成功数
     */
    private Integer totalSuccess;

    /**
     * 当日文章浏览数
     */
    private Integer articleViewToday;

    /**
     * 文章浏览总数
     */
    private Integer articleViewTotal;

    /**
     * 当日新增用户数
     */
    private Integer userToday;

    /**
     * 用户总数
     */
    private Integer userTotal;
}
