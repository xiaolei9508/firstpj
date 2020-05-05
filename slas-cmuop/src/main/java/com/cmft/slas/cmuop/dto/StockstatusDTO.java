package com.cmft.slas.cmuop.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.cmft.slas.cmuop.common.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel(value = "StockstatusDTO", description = "股票信息")
@EqualsAndHashCode(callSuper = false)
public class StockstatusDTO extends BaseDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
    /**
    * 主键id
    */
    private Long tStockStatusId;
    
    /**
    * 股票代码
    */
    @ApiModelProperty(value = "股票代码")
    private String stockCode;
    
    /**
    * 股票类型
    */
    @ApiModelProperty(value = "股票类型")
    private String stockType;
    
    /**
    * 股票名
    */
    @ApiModelProperty(value = "股票名")
    private String stockName;
    
    /**
    * 实体代码
    */
    @ApiModelProperty(value = "实体代码")
    private String entityCode;
    
    /**
    * 股票净值
    */
    @ApiModelProperty(value = "股票净值")
    private Double index;
    
    /**
    * 涨跌幅
    */
    @ApiModelProperty(value = "涨跌幅")
    private Double growth;
    
	/**
    * 是否同步
    */
    @ApiModelProperty(value = "是否同步")
    private Byte sync;
    
	/**
    * 股票状态：0未开盘，1已开盘
    */
    @ApiModelProperty(value = "股票状态：0未开盘，1已开盘")
    private Byte status;
    
	/**
    * 领导人排序,99为未设定
    */
    @ApiModelProperty(value = "领导人排序,99为未设定")
    private Byte orderNum;
	
	/**
    * 最新交易日
    */ 
    @ApiModelProperty(value = "最新交易日")    
	private Date latestTradeDate;
    
	/**
    * 是否展示
    */
    @ApiModelProperty(value = "是否展示")
    private Byte ifShow;
}
