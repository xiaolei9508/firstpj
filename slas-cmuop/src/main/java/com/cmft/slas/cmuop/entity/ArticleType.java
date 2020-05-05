package com.cmft.slas.cmuop.entity;

import com.cmft.slas.cmuop.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author liurp001
 * @Since 2020/1/4
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "t_article_type")
public class ArticleType  extends BaseEntity {
    @Id
    private Long articleEntityId;

    private String articleId;

    private String typeId;
}
