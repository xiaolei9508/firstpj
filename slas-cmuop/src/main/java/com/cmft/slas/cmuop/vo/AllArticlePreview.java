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
public class AllArticlePreview extends ArticlePreview{
    /**
     * 是否分类置顶
     */
    private Boolean ifPartStick;

    /**
     * 是否全部置顶
     */
    private Boolean ifAllStick;

    /**
     * oa重要度
     */
    private Integer oaImportance;

    /**
     * 评分
     */
    private Float contentQuality;

    /**
     * 是否推荐
     */
    private Boolean ifRecommend;

    /**
     * 序号
     */
    private Integer sortNum;

    /**
     * 是否置顶到轮播
     */
    private Boolean ifStick;

    private String orderNum;
}
