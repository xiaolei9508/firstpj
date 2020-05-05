package com.cmft.slas.cmuop.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.cmft.slas.cmuop.common.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Table(name = "t_user_like_comment")
@EqualsAndHashCode(callSuper = false)
@ToString
@Accessors(chain = true)
public class UserLikeComment extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
        
    /**
    * 主键id
    */
    @Id
  	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userLikeId;

    private Long commentId;
    
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
