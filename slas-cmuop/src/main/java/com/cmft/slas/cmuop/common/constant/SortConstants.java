package com.cmft.slas.cmuop.common.constant;

import com.tdunning.math.stats.Sort;

public enum SortConstants{
    NORMAL(0),
    RUNOUT(1),
    REFRESH(2),
    // 卡片大小
    CARD_SIZE(3);
    Integer value;
    SortConstants(Integer value){
        this.value = value;
    }

    public Integer getValue(){
        return value;
    }

    public static SortConstants getByValue(Integer value){
        for (SortConstants sort : SortConstants.values()){
            if (sort.value.equals(value)){
                return sort;
            }
        }
        return null;
    }
}