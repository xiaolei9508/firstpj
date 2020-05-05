package com.cmft.slas.cmuop.common.constant;

public enum LeaderOrder{
    DEFAULT(99),
    DATELIMIT(3);
    Integer value;
    LeaderOrder(Integer value){
        this.value = value;
    }

    public Integer getValue(){
        return this.value;
    }
}
