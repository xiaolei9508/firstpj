package com.cmft.slas.cmuop.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Author liurp001
 * @Since 2019/12/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AllArticleDetail extends ArticleDetail{
    /**
     * 是否分类置顶
     */
    private Boolean ifPartStick;

    /**
     * 是否全部置顶
     */
    private Boolean ifAllStick;

    private Boolean ifStick;
}
