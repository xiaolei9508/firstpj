package com.cmft.slas.cmuop.dto;

import javax.persistence.Table;

import com.cmft.slas.cmuop.common.dto.BaseDTO;
import com.cmft.slas.cmuop.common.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Author liurp001
 * @Since 2020/4/23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class NotificationDTO extends BaseDTO {

    private String notificationType;

    private String articleId;

    private Boolean isRead;
}
