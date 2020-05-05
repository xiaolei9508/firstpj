package com.cmft.slas.cmuop.mapper;

import com.cmft.slas.cmuop.common.config.db.CommonMapper;
import com.cmft.slas.cmuop.entity.Notification;
import com.cmft.slas.cmuop.vo.NotificationPreview;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author liurp001
 * @Since 2020/4/23
 */
public interface NotificationMapper extends CommonMapper<Notification> {
    List<NotificationPreview> getNotifications(@Param("isRead") Boolean isRead);
}
