package com.cmft.slas.cmuop.entity;

import java.io.Serializable;

import javax.persistence.*;

import com.cmft.slas.cmuop.common.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Table(name = "t_user_comment")
@EqualsAndHashCode(callSuper = false)
@ToString
public class UserComment extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
        
    /**
    * 主键id
    */
    @Id
  	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
    * 
    */
    private String comment;
    
    /**
    * 回复评论id
    */
    private Long replyId;

    private String replyName;

    /**
     * 根评论id
     */
    private Long rootId;

    private Byte isDelete;
}
