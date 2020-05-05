 package com.cmft.slas.cmuop.vo;

 import lombok.Data;
 import lombok.EqualsAndHashCode;
 import lombok.experimental.Accessors;

 import java.io.Serializable;

 @Data
 @EqualsAndHashCode(callSuper = false)
 @Accessors(chain = true)
 public class UserVO implements Serializable {

     private static final long serialVersionUID = 8165086531799929016L;

     // nuc Id for user
     private String uid;

     // the name of the user
     private String name;

     // the user's image url
     private String imgUrl;
 }
