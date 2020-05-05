package com.cmft.slas.cmuop.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.cmft.slas.cmuop.common.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Table(name = "t_user_not_like")
@EqualsAndHashCode(callSuper = false)
@ToString
public class UserNotLike extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
        
    /**
    * 
    */
    @Id
  	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNotLikeId;
    
    /**
    * 
    */
    private String articleId;
    
    /**
    * 
    */
    private String uid;
    
    /**
    * 原因
    */
    private String reason;
}
