package com.cmft.slas.cmuop.entity;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cmft.slas.cmuop.common.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Table(name = "t_article_related_entity")
@EqualsAndHashCode(callSuper = false)
@ToString
@Accessors(chain = true)
public class ArticleRelatedEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
        
    /**
    * 主键
    */
    @Id
  	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleEntityId;
    
    /**
    * 文章id
    */
    private String articleId;
    
    /**
    * 组织编码
    */
    private String entityCode;

    /**
     * 所属分类
     */
    private String columnType;

    /**
     * 序号
     */
    private Integer orderNum;
}
