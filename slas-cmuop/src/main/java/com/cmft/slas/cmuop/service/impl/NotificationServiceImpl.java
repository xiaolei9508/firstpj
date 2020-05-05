package com.cmft.slas.cmuop.service.impl;

import com.cmft.slas.cmuop.common.constant.NotificationTypes;
import com.cmft.slas.cmuop.dto.NotificationDTO;
import com.cmft.slas.cmuop.entity.Article;
import com.cmft.slas.cmuop.entity.Notification;
import com.cmft.slas.cmuop.mapper.ArticleMapper;
import com.cmft.slas.cmuop.mapper.NotificationMapper;
import com.cmft.slas.cmuop.service.NotificationService;
import com.cmft.slas.cmuop.vo.NotificationPreview;
import com.cmft.slas.cmuop.vo.ReadStatDTO;
import com.cmft.slas.common.pojo.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author liurp001
 * @Since 2020/4/23
 */
@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public PageInfo<NotificationPreview> getNotificationPage(Page page, Boolean isRead) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<NotificationPreview> list = notificationMapper.getNotifications(isRead);
        PageInfo<NotificationPreview> previews = new PageInfo<>(list);
        processMessage(previews.getList());
        return previews;
    }

    private void processMessage(List<NotificationPreview> previews) {
        if (CollectionUtils.isEmpty(previews))
            return;
        Map<String, List<NotificationPreview>> previewMap =
            previews.stream().collect(Collectors.groupingBy(NotificationPreview::getArticleId));
        Example example = new Example(Article.class);
        example.createCriteria().andIn("articleId", previewMap.keySet());
        List<Article> articles = articleMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(articles))
            return;
        Map<String, Article> articleMap =
            articles.stream().collect(Collectors.toMap(Article::getArticleId, value -> value));
        previewMap.forEach((articleId, list) -> list.forEach(preview -> {
            preview.setPubTime(articleMap.get(articleId).getPubTime());
            preview.setMessage(getMessage(preview.getNotificationType(), articleMap.get(articleId)));
        }));
    }

    private String getMessage(String notificationType, Article article) {
        NotificationTypes type = NotificationTypes.getType(notificationType);
        if (type == null)
            return "";
        return String.format(type.getMsg(), article.getTitle());
    }

    @Override
    public String insertNotification(NotificationDTO dto) {
        log.info("notification received: articleId {}, type {}", dto.getArticleId(), dto.getNotificationType());
        Notification notification = new Notification();
        BeanUtils.copyProperties(dto, notification);
        int count = notificationMapper.insertSelective(notification);
        return count > 0 ? "操作成功" : "操作失败";
    }

    @Override
    public String updateNotification(Long notificationId, NotificationDTO dto) {
        Notification record = new Notification();
        BeanUtils.copyProperties(dto, record);
        Example example = new Example(Notification.class);
        example.createCriteria().andEqualTo("tNotificationId", notificationId);
        int count = notificationMapper.updateByExampleSelective(record, example);
        return count > 0 ? "操作成功" : "操作失败";
    }

    @Override
    public String readMessage(Long notificationId) {
        Example example = new Example(Notification.class);
        example.createCriteria().andEqualTo("tNotificationId", notificationId).andEqualTo("isRead", true);
        if (notificationMapper.selectCountByExample(example) > 0)
            return "消息已被阅读";
        NotificationDTO dto = new NotificationDTO().setIsRead(true);
        return updateNotification(notificationId, dto);
    }

    @Override
    public ReadStatDTO readStat() {
        ReadStatDTO dto = new ReadStatDTO();
        Example readEg = new Example(Notification.class);
        readEg.createCriteria().andEqualTo("isDelete", false).andEqualTo("isRead", true);
        Example unReadEg = new Example(Notification.class);
        unReadEg.createCriteria().andEqualTo("isDelete", false).andEqualTo("isRead", false);
        dto.setReadCount(notificationMapper.selectCountByExample(readEg));
        dto.setUnreadCount(notificationMapper.selectCountByExample(unReadEg));
        return dto;
    }
}
