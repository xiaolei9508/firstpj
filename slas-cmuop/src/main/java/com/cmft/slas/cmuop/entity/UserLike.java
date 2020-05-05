package com.cmft.slas.cmuop.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.cmft.slas.cmuop.common.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Table(name = "t_user_like")
@EqualsAndHashCode(callSuper = false)
@ToString
public class UserLike extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
        
    /**
    * 
    */
    @Id
  	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userLikeId;
    
    /**
    * 文章id

    */
    private String articleId;
    
    /**
    * nuc uid
    */
    private String uid;
    
	/**
    * 操作类型
    */
    private Byte type;
}
