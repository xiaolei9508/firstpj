package com.cmft.slas.cmuop.vo;

import java.util.Date;

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
public class SelectedDetail extends ArticleDetail{

    /**
     * 是否置顶到轮播
     */
    private Boolean ifStick;

    /**
     * 置顶到轮播时间
     */
    private Date stickUpdateTime;
}
