package com.cmft.slas.cmuop.common.constant;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author liurp001
 * @Since 2020/4/23
 */
public enum NotificationTypes {
    CMGOfficial_Insert("1", "官网要闻\"%s\"入库"), OAUpdate("2", "OA文章\"%s\"更新"), CMGOfficial_update("3", "官网要闻\"%s\"更新");

    String type;
    String msg;

    NotificationTypes(String type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public static NotificationTypes getType(String type) {
        if (StringUtils.isBlank(type))
            return null;
        for (NotificationTypes notification : NotificationTypes.values()) {
            if (type.equals(notification.type))
                return notification;
        }
        return null;
    }

    public String getMsg() {
        return this.msg;
    }

    public String getType() {
        return this.type;
    }
}
