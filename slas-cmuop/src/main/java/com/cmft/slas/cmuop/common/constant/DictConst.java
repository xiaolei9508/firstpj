package com.cmft.slas.cmuop.common.constant;

/**
 * @Author liurp001
 * @Since 2020/1/9
 */
public enum DictConst {
    THIRD_LEVEL("third_level"),
    ENTITY_LEVEL("entity_level"),
    POINT_OF_VIEW("point_of_view"),
    ENTITY_ENTREPRENEUR("entity_entrepreneur"),
    ZHAOWEN_ENTREPRENEUR("zhaowen_entrepreneur"),
    TRUST_LEVEL("trust_level"),
    NOT_LIKE_REASON("not_like_reason"),
    ORIGIN("origin"),
    TYPE_OF_CONTENT("type_of_content");

    String code;

    DictConst(String code){
        this.code = code;
    }

    public String getCode(){
        return code;
    }
}
