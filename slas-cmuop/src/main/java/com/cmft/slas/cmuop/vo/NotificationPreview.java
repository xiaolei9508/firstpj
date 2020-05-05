package com.cmft.slas.cmuop.vo;

import com.cmft.slas.cmuop.dto.NotificationDTO;
import com.cmft.slas.cmuop.entity.Notification;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Author liurp001
 * @Since 2020/4/23
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class NotificationPreview extends Notification {

    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pubTime;
}
