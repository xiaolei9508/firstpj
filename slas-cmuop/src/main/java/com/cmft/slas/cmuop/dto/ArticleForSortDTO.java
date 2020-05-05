package com.cmft.slas.cmuop.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleForSortDTO extends ArticleDTO implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    public interface TimeStep {
        public static final int LEVEL6 = 6;
        public static final int LEVEL5 = 5;
        public static final int LEVEL4 = 4;
        public static final int LEVEL3 = 3;
        public static final int LEVEL2 = 2;
        public static final int LEVEL1 = 1;
        public static final int LEVEL0 = 0;
    }

    private Long tArticleId;

    private String entityLevel;

    private Boolean ifRead;

    private Boolean ifEvent;

    private Boolean ifManager;

    // 所属三级分类
    private String columnType;

    private Integer timeStep;

    private Integer entityStep;

    private Boolean entityMain;

    private Byte ifPartStick;

    private Byte ifAllStick;
}
