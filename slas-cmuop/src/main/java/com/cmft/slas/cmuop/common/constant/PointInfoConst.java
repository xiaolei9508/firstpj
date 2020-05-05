package com.cmft.slas.cmuop.common.constant;

import org.apache.commons.lang3.StringUtils;

public enum PointInfoConst {
    THEMATIC("1"),
    MONTHLY("2"),
    WEEKLY("3"),
    OTHER("4"), REDIS_KEY("slas:cmuop:pointInfo");

    String value;

    PointInfoConst(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean isValid(String value) {
        if(StringUtils.isBlank(value)) {
            return false;
        }
        for (PointInfoConst type : values()) {
            if(type.value.equals(value)){
                return true;
            }
        }
        return false;
    }
}
