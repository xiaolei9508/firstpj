package com.cmft.slas.cmuop.common.constant;

public enum Entity {
    CMG("001");
    String code;
    Entity(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
}
