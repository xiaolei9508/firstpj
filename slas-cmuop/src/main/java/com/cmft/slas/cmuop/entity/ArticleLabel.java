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

@Data
@Table(name = "t_article_label")
@EqualsAndHashCode(callSuper = false)
@ToString
public class ArticleLabel extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleLabelId;

    /**
     * 文章id\n
     */
    private String articleId;

    /**
     * 标签
     */
    private String labelId;

    /**
     * 标签类型
     * 
     */
    private String labelType;
}
