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

@Data
@Table(name = "t_cmuop_point_view_record")
@EqualsAndHashCode(callSuper = false)
@ToString
public class PointViewRecord extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long viewRecordId;

    private Long infoId;

    private String infoType;

    private String uid;
}