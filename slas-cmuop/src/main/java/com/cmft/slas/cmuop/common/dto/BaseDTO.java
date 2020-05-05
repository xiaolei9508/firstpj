 package com.cmft.slas.cmuop.common.dto;

 import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
 /**
  * 包含共有信息的实体类
  * @author ex-liwt001
  *
  */
 @Data
 public class BaseDTO implements Serializable {

     /** 
      * 
      */
     private static final long serialVersionUID = -2445940170185813867L;
     
     /**
      * 是否有效 0有效 1无效
      */
     @ApiModelProperty(value = "是否有效 0有效 1无效")
     private Byte isDelete;

     /**
      * 创建人
      */
     @ApiModelProperty(value = "创建人")
     private String createOperator;

     /**
      * 创建日期
      */
     @ApiModelProperty(value = "创建日期")
     private Date createTime;

     /**
      * 变更人
      */
     @ApiModelProperty(value = "变更人")
     private String updateOperator;

     /**
      * 变更时间
      */
     @ApiModelProperty(value = "变更时间")
     private Date updateTime;
     
 }
