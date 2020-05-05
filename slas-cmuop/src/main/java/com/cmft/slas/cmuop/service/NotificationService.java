package com.cmft.slas.cmuop.service;

import com.cmft.slas.cmuop.dto.NotificationDTO;
import com.cmft.slas.cmuop.vo.NotificationPreview;
import com.cmft.slas.cmuop.vo.ReadStatDTO;
import com.cmft.slas.common.pojo.Page;
import com.github.pagehelper.PageInfo;

/**
 * @Author liurp001
 * @Since 2020/4/23
 */
public interface NotificationService {

    PageInfo<NotificationPreview> getNotificationPage(Page page, Boolean isRead);

    String insertNotification(NotificationDTO dto);

    String updateNotification(Long notificationId, NotificationDTO dto);

    String readMessage(Long notificationId);

    ReadStatDTO readStat();
}
