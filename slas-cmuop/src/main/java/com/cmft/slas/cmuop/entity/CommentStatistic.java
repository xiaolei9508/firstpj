package com.cmft.slas.cmuop.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.cmft.slas.cmuop.common.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Table(name = "t_comment_statistic")
@EqualsAndHashCode(callSuper = false)
@ToString
public class CommentStatistic extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
        
    /**
    * 主键id
    */
    @Id
  	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentStatisticId;
    
    /**
    * 评论id

    */
    private Long commentId;
    
    /**
    * 点赞数量
    */
    private Integer likeNum;
}
