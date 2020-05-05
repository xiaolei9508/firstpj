package com.cmft.slas.cmuop.common.entity;

import java.util.Date;

import lombok.Data;

/**
 * @author xiaojp001
 * @date 2018/05/03
 */
@Data
public class BaseEntity {

    /**
     * 是否有效 0有效 1无效
     */
    private Byte isDelete;

    /**
     * 创建人
     */
    private String createOperator;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 变更人
     */
    private String updateOperator;

    /**
     * 变更时间
     */
    private Date updateTime;
}
