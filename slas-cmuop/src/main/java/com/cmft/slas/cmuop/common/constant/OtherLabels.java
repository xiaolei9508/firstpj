package com.cmft.slas.cmuop.common.constant;

/**
 * @Author liurp001
 * @Since 2020/4/26
 */
public enum OtherLabels {

    CMGSpecial("集团要闻");

    String label;

    OtherLabels(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
