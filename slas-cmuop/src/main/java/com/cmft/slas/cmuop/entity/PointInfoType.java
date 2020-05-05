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

@Data
@Table(name = "t_cmuop_point_info_type")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ToString
public class PointInfoType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String infoType;
    private String infoTypeName;
    private Integer sort;
    private String imgUrl;
    private Boolean haveOutline;
    private Boolean ifShow;
}
