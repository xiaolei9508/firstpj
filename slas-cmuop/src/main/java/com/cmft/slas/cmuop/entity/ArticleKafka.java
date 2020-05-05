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
@Table(name = "t_article_kafka")
@EqualsAndHashCode(callSuper = false)
@ToString
@Accessors(chain = true)
public class ArticleKafka extends BaseEntity implements Serializable {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tArticleKafkaId;

    /**
     * 文章id
     */
    private String articleId;

    /**
     * 是否同步失败
     */
    private Boolean ifFail;

    /**
     * 错误原因
     */
    private String reason;
}
