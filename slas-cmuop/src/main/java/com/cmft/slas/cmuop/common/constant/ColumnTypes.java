package com.cmft.slas.cmuop.common.constant;

public enum ColumnTypes {
    RECOMMEND("0"),
    LEADER("1"),
    COMPANY("2"),
    INDUSTRY("3"),
    POINTOFVIEW("4"),
    STOCK("5");
    String value;

    ColumnTypes(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
