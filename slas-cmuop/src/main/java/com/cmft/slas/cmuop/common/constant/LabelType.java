package com.cmft.slas.cmuop.common.constant;

/**
 * @Author liurp001
 * @Since 2020/1/15
 */
public enum LabelType {
    USER("user"), LABEL("label"), ENTITY("entity"), OTHER("other");

    public String code;

     LabelType(String code){
        this.code = code;
    }

    public static LabelType getByCode(String code){
         for (LabelType type : values()){
             if(type.code.equals(code)){
                 return type;
             }
         }
         return null;
    }

    public String getCode(){
         return this.code;
    }
}
