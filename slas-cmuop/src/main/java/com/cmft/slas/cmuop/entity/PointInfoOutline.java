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
@Table(name = "t_cmuop_point_info_outline")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ToString
public class PointInfoOutline extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outlineId;

    private Long infoId;
    private String infoType;
    private String outline;
    private Integer orderNum;
}