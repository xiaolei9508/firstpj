package com.cmft.slas.cmuop.common.constant;

/**
 * @Author liurp001
 * @Since 2020/1/6
 */
public enum WebSortType {

    PUBDATE("pubDate","pub_date"),
    PUBTIME("pubTime", "pub_time"),
    VIEWCOUNT("viewCount","view_num"),
    UPDATETIME("updateTime", "a.update_time"),
    OAIMPORTANCE("oaImportance","oa_importance");

    final String value;

    final String name;

    WebSortType(String name, String value){
        this.value = value;
        this.name = name;
    }

    public static String getValue(String column){
        for(WebSortType sort : WebSortType.values()){
            if(sort.name.equals(column))
                return sort.value;
        }
        return null;
    }
}
