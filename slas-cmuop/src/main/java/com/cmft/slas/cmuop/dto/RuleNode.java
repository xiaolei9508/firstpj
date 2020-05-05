package com.cmft.slas.cmuop.dto;

import lombok.Data;

@Data
public class RuleNode {
    private RuleNode leftNode;
    private RuleNode rightNode;
    private String value;

    // (1=列名,2=int,3=string,4=boolean,5=list,6=logic)
    private Integer valueType;

}
