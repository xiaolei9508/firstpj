package com.cmft.slas.cmuop.common.constant;

/**
 * @Author liurp001
 * @Since 2020/4/26
 */
public enum ArticleSource {
    CMG("招商局集团");

    String source;

    ArticleSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return this.source;
    }
}
