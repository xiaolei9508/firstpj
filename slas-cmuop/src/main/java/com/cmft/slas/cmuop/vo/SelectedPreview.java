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
public class SelectedPreview extends ArticlePreview{
    /**
     * 是否置顶到轮播
     */
    private Boolean ifStick;
}
