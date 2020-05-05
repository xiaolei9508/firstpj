package com.cmft.slas.cmuop.common.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "TimeRange", description = "时间范围对象")
public class TimeRange implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 9130386371766359110L;

    @ApiModelProperty(value = "时间范围起")
    private Date from;

    @ApiModelProperty(value = "时间范围止")
    private Date to;

    public Date getFrom()
    {
        return from;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public void setFrom(Date from)
    {
        this.from = from;
    }

    public Date getTo()
    {
        return to;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public void setTo(Date to)
    {
        this.to = to;
    }

}
