package com.cmft.slas.cmuop.entity;

import com.cmft.slas.cmuop.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Table;

/**
 * @Author liurp001
 * @Since 2020/4/23
 */
@Data
@Table(name = "t_notification")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Notification extends BaseEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long tNotificationId;

    private String notificationType;

    private String articleId;

    private Boolean isRead;
}
