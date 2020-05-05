package com.cmft.slas.cmuop.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.cmft.slas.cmuop.common.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Table(name = "t_article_authorized_entities")
@EqualsAndHashCode(callSuper = false)
@ToString
public class ArticleAuthorizedEntities extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
        
    /**
    * 主键
    */
    @Id
  	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleViewId;
    
    /**
    * 文章id
    */
    private String articleId;
    
    /**
    * 组织编码
    */
    private String entityCode;
    
    /**
    * 分类权限
    */
    private String authAreaId;
}
