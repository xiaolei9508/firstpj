package com.cmft.slas.cmuop.quartz.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmft.slas.cmuop.dto.UserCommentDTO;
import com.cmft.slas.cmuop.dto.UserLikeCommentDTO;
import com.cmft.slas.cmuop.dto.UserLikeDTO;
import com.cmft.slas.cmuop.dto.UserViewDTO;
import com.cmft.slas.cmuop.entity.ArticleStatistic;
import com.cmft.slas.cmuop.entity.CommentStatistic;
import com.cmft.slas.cmuop.service.*;

/**
 * 示例quartz job
 *
 * @author xiaojp001
 * @date 2018/05/29
 */
@Slf4j
@DisallowConcurrentExecution
public class CountArticleLikeJob implements Job {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserLikeService userLikeService;
    @Autowired
    private ArticleStatisticService articleStatisticService;
    @Autowired
    private CommentStatisticService commentStatisticService;
    @Autowired
    private UserViewService userViewService;
    @Autowired
    private UserCommentService userCommentService;
    @Autowired
    private UserLikeCommentService userLikeCommentService;

    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("article counter started");
        // 获得上次定时器启动时间 获取不到使用当前时间 避免重复计算
        Date previousFireTime = context.getPreviousFireTime() == null ? new Date() : context.getPreviousFireTime();
        // 获得本次定时器启动时间 获取不到使用当前时间
        Date fireTime = context.getFireTime() == null ? new Date() : context.getFireTime();

        log.info("counter started at previous fire time {}, fire at {}", df.format(previousFireTime),
            df.format(fireTime));

        updateUserLike(previousFireTime, fireTime);
        updateUserView(previousFireTime, fireTime);
        updateUserComment(previousFireTime, fireTime);
        updateUserLikeComment(previousFireTime, fireTime);
        log.info("article counter completed");
    }

    /*更新用户点赞*/
    private void updateUserLike(Date previousFireTime, Date fireTime) {

        List<UserLikeDTO> userLikeList = userLikeService.queryByArticle(previousFireTime, fireTime);
        // 将list转化为map
        Map<String, List<UserLikeDTO>> likesMap =
            userLikeList.stream().collect(Collectors.groupingBy(UserLikeDTO::getArticleId));
        for (String articleId : likesMap.keySet()) {
            Integer score = 0;
            for (UserLikeDTO dto : likesMap.get(articleId)) {
                score += dto.getType();// 获得增量
            }
            ArticleStatistic articleStatistic = new ArticleStatistic();
            articleStatistic.setArticleId(articleId);
            ArticleStatistic statistic = articleStatisticService.selectOne(articleStatistic);

            if (statistic == null) {
                articleStatistic.setLikeNum(score);
                articleStatisticService.addArticleStatistic(articleStatistic);
            } else {
                statistic.setLikeNum(statistic.getLikeNum() + score);
                statistic.setArticleStatisticId(statistic.getArticleStatisticId());
                articleStatisticService.updateArticleStatistic(statistic);
            }
        }

    }

    /*更新用户浏览*/
    private void updateUserView(Date previousFireTime, Date fireTime) {

        List<UserViewDTO> userViewList = userViewService.queryByArticle(previousFireTime, fireTime);
        for (UserViewDTO dto : userViewList) {
            ArticleStatistic articleStatistic = new ArticleStatistic();
            articleStatistic.setArticleId(dto.getArticleId());

            ArticleStatistic statistic = articleStatisticService.selectOne(articleStatistic);
            if (statistic == null) {
                articleStatistic.setViewNum(dto.getCount());
                articleStatisticService.addArticleStatistic(articleStatistic);
            } else {
                articleStatistic.setViewNum(statistic.getViewNum() + dto.getCount());
                articleStatistic.setArticleStatisticId(statistic.getArticleStatisticId());
                articleStatisticService.updateArticleStatistic(articleStatistic);
            }
        }
    }

    /*更新用户评论*/
    private void updateUserComment(Date previousFireTime, Date fireTime) {
        List<UserCommentDTO> userCommentList = userCommentService.queryByArticle(previousFireTime, fireTime);
        for (UserCommentDTO dto : userCommentList) {
            ArticleStatistic articleStatistic = new ArticleStatistic();
            articleStatistic.setArticleId(dto.getArticleId());
            ArticleStatistic statistic = articleStatisticService.selectOne(articleStatistic);
            if (statistic == null) {
                articleStatistic.setCommentNum(dto.getCount());
                articleStatisticService.addArticleStatistic(articleStatistic);
            } else {
                articleStatistic.setCommentNum(statistic.getCommentNum() + dto.getCount());
                articleStatistic.setArticleStatisticId(statistic.getArticleStatisticId());
                articleStatisticService.updateArticleStatistic(articleStatistic);
            }
        }
    }

    /*更新用户点赞评论*/
    private void updateUserLikeComment(Date previousFireTime, Date fireTime) {
        List<UserLikeCommentDTO> userLikeCommentList =
            userLikeCommentService.queryByComment(previousFireTime, fireTime);
        // 将list转化为map
        Map<Long, List<UserLikeCommentDTO>> likesMap =
            userLikeCommentList.stream().collect(Collectors.groupingBy(UserLikeCommentDTO::getCommentId));
        for (Long articleId : likesMap.keySet()) {
            Integer score = 0;
            // 获得增量
            for (UserLikeCommentDTO dto : likesMap.get(articleId)) {
                score += dto.getType();
            }
            CommentStatistic commentStatistic = new CommentStatistic();
            commentStatistic.setCommentId(articleId);
            CommentStatistic statistic = commentStatisticService.selectOne(commentStatistic);

            if (statistic == null) {
                commentStatistic.setLikeNum(score);
                commentStatisticService.addCommentStatistic(commentStatistic);
            } else {
                commentStatistic.setLikeNum(statistic.getLikeNum() + score);
                commentStatistic.setCommentStatisticId(statistic.getCommentStatisticId());
                commentStatisticService.updateCommentStatistic(commentStatistic);
            }
        }
    }
}
