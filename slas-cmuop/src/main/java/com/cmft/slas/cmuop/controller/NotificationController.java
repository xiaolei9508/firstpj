package com.cmft.slas.cmuop.controller;

import com.cmft.slas.cmuop.common.constant.NotifiedPersons;
import com.cmft.slas.cmuop.dto.NotificationDTO;
import com.cmft.slas.cmuop.service.NotificationService;
import com.cmft.slas.cmuop.vo.NotificationPreview;
import com.cmft.slas.cmuop.vo.ReadStatDTO;
import com.cmft.slas.common.pojo.Page;
import com.cmft.slas.common.pojo.WebResponse;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author liurp001
 * @Since 2020/4/23
 */
@RestController
@RequestMapping("/headline/cmuop/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/list")
    public WebResponse<PageInfo<NotificationPreview>> getNotificationList(@ModelAttribute Page page,
        @RequestParam(required = false) Boolean isRead, HttpServletRequest request) {
        String uid = request.getHeader("uid");
        if (!NotifiedPersons.notified(uid))
            return WebResponse.success(new PageInfo<>());
        PageInfo<NotificationPreview> preview = notificationService.getNotificationPage(page, isRead);
        return preview == null ? WebResponse.error("操作失败", null) : WebResponse.success(preview);
    }

    @PutMapping("/{notificationId}/read")
    public WebResponse<String> readNotification(@PathVariable("notificationId") Long notificationId) {
        String body = notificationService.readMessage(notificationId);
        return "操作失败".equals(body) ? WebResponse.error(body, body) : WebResponse.success(body, body);
    }

    @GetMapping("/stat")
    public WebResponse<ReadStatDTO> readStat(HttpServletRequest request) {
        String uid = request.getHeader("uid");
        if (!NotifiedPersons.notified(uid))
            return WebResponse.success(new ReadStatDTO().setReadCount(0).setUnreadCount(0));
        return WebResponse.success(notificationService.readStat());
    }
}
