package com.cmft.slas.cmuop.vo;

import com.cmft.slas.cmuop.common.entity.BaseEntity;
import com.cmft.slas.cmuop.entity.PointInfoOutline;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@ToString
public class PointInfoVO extends BaseEntity {

    private Long infoId;
    private String infoType;
    private String title;
    private String pubOrg;
    private String pubDate;
    private Date pubTime;
    private String imgUrl;
    private String fileName;
    private String fileUrl;
    private Integer pageCount;
    private Integer viewCount;
    private Integer orderNum;
    private Boolean ifShow;
    private Boolean ifRead;

    private List<String> outlineList;
}
