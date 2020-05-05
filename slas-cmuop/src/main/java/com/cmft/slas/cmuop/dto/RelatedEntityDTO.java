package com.cmft.slas.cmuop.dto;

import java.util.List;

import lombok.Data;

@Data
public class RelatedEntityDTO {
    private String parentCode;
    private List<String> childrenList;
}
