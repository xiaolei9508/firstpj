package com.cmft.slas.cmuop.entity;

import com.cmft.slas.cmuop.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "t_cmuop_point_info")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ToString
public class PointInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long infoId;

    private String infoType;
    private String title;
    private String pubOrg;
    private String pubDate;
    private Date   pubTime;
    private String imgUrl;
    private String fileName;
    private String fileUrl;
    private Integer pageCount;
    private Integer viewCount;
    private Integer orderNum;
    private Boolean ifShow;
}
