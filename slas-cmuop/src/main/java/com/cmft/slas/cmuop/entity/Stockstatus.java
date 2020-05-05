package com.cmft.slas.cmuop.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.cmft.slas.cmuop.common.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Table(name = "t_stock_status")
@EqualsAndHashCode(callSuper = false)
@ToString
public class Stockstatus extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
        
    /**
    * 主键id
    */
    @Id
  	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tStockStatusId;
    
    /**
    * 股票代码
    */
    private String stockCode;
    
    /**
    * 股票类型
    */
    private String stockType;
    
    /**
    * 股票名
    */
    private String stockName;
    
    /**
    * 实体代码
    */
    private String entityCode;
    
    /**
    * 股票净值
    */
    private Double index;
    
    /**
    * 涨跌幅
    */
    private Double growth;
    
	/**
    * 是否同步
    */
    private Byte sync;
    
	/**
    * 股票状态：0未开盘，1已开盘
    */
    private Byte status;
    
	/**
    * 领导人排序,99为未设定
    */
    private Byte orderNum;
	
	/**
    * 最新交易日
    */     
	private Date latestTradeDate;
    
	/**
    * 是否展示
    */
    private Byte ifShow;
}
