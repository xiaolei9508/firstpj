package com.cmft.slas.cmuop.processor.processorImpl;

import com.cmft.slas.cmuop.common.constant.ArticleSource;
import com.cmft.slas.cmuop.common.constant.LabelType;
import com.cmft.slas.cmuop.common.constant.NotificationTypes;
import com.cmft.slas.cmuop.common.constant.OtherLabels;
import com.cmft.slas.cmuop.dto.NotificationDTO;
import com.cmft.slas.cmuop.entity.Article;
import com.cmft.slas.cmuop.entity.ArticleLabel;
import com.cmft.slas.cmuop.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author liurp001
 * @Since 2020/4/26
 */
@Component
@Slf4j
public class NotificationDistributor {
    @Autowired
    private NotificationService notificationService;

    public void sendNotification(Article article, List<ArticleLabel> articleLabels) {
        NotificationDTO dto = null;
        if(CMGSpecial(article, articleLabels)){
            dto =  createNotification(NotificationTypes.CMGOfficial_update.getType(), article.getArticleId());
        }

        if (dto != null) {
            notificationService.insertNotification(dto);
            log.info("Notification inserted: type: {}, articleId {}", dto.getNotificationType(), dto.getArticleId());
        }
    }

    private Boolean CMGSpecial(Article article, List<ArticleLabel> articleLabels){
        if(CollectionUtils.isEmpty(articleLabels))
            return false;
        Map<String, List<ArticleLabel>> labelMap = articleLabels.stream().collect(Collectors.groupingBy(ArticleLabel::getLabelType));
        List<ArticleLabel> otherLabels = labelMap.get(LabelType.OTHER.getCode());
        if(CollectionUtils.isEmpty(otherLabels))
            return false;
        boolean haveLabel = otherLabels.stream().map(ArticleLabel::getLabelId).distinct().collect(Collectors.toList()).contains(OtherLabels.CMGSpecial.getLabel());
        return haveLabel && ArticleSource.CMG.getSource().equals(article.getSource());
    }

    private NotificationDTO createNotification(String type, String articleId) {
        return new NotificationDTO().setArticleId(articleId).setNotificationType(type);
    }
}
