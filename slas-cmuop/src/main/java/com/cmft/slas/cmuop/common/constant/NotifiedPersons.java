package com.cmft.slas.cmuop.common.constant;

/**
 * @Author liurp001
 * @Since 2020/4/23
 */
public enum NotifiedPersons {
    guxj001_di("55f9115b-2539-4ac8-9e02-6248b435cee4"), guxj001_prd("e481e954-018f-46ca-9d39-712006af7e1a"),
    zhangd001_di("ca8cbaf3-b6c5-4dde-9138-7354e69fe5ce"), zhangd001_prd("8b0f46b0-8db2-4415-836b-eb4a8df8317a"),
    ex_zhengrx001_prd("66f74070-39b4-4ad9-af20-b5708ab94c4c");

    String uid;

    NotifiedPersons(String uid) {
        this.uid = uid;
    }

    public static Boolean notified(String uid) {
        for (NotifiedPersons person : NotifiedPersons.values()) {
            if (person.uid.equals(uid))
                return true;
        }
        return false;
    }
}
