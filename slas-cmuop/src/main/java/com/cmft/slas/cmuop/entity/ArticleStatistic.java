package com.cmft.slas.cmuop.entity;

import com.cmft.slas.cmuop.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "t_article_statistic")
@EqualsAndHashCode(callSuper = false)
@ToString
@Accessors(chain = true)
public class ArticleStatistic extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
        
    /**
    * 主键id
    */
    @Id
  	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleStatisticId;
    
    /**
    * 文章id
    */
    private String articleId;
    
    /**
    * 点赞数量
    */
    private Integer likeNum;
    
    /**
    * 收藏数量
    */
    private Integer enshrineNum;
    
    /**
    * 评论数

    */
    private Integer commentNum;
    
    /**
    * 浏览量
    */
    private Integer viewNum;
}
