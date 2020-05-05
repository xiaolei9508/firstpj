package com.cmft.slas.cmuop.vo;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserViewVO {
    private static final long serialVersionUID = 8165086531799929016L;

    /**
     * 文章id
     */
    private String articleId;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 阅读时间
     */
    private Date time;

}
