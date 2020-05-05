package com.cmft.slas.cmuop.common.constant;

public enum FakeEntity {

    ZHAOWEN("000");

    String code;
    FakeEntity(String code){
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }
}
