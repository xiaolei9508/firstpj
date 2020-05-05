package com.cmft.slas.cmuop.processor.processorImpl;

import com.cmft.slas.cmuop.entity.ArticleStatistic;
import com.cmft.slas.cmuop.entity.UserComment;
import com.cmft.slas.cmuop.mapper.ArticleStatisticMapper;
import com.cmft.slas.cmuop.mapper.UserCommentMapper;
import com.cmft.slas.cmuop.mapper.UserLikeMapper;
import com.cmft.slas.cmuop.processor.ArticleProcessor;
import com.cmft.slas.cmuop.service.SearchFromESService;
import com.cmft.slas.cmuop.vo.ArticleVO;
import com.cmft.slas.cmuop.vo.CommentVO;
import com.cmft.slas.cmuop.vo.UserVO;
import com.cmft.slas.es.domain.ArticleContent;
import com.cmft.slas.recommendation.common.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author liurp001
 * @Since 2020/1/2
 */
@Component
public class ArticleAppProcessor implements ArticleProcessor {

    @Autowired
    private UserLikeMapper userLikeMapper;

    @Autowired
    private UserCommentMapper userCommentMapper;

    @Autowired
    private CommentAppProcessor commentAppProcessor;

    @Autowired
    private UserAppProcessor userAppProcessor;

    @Autowired
    private SearchFromESService searchFromESService;

    @Autowired
    private ArticleStatisticMapper articleStatisticMapper;

    @Autowired
    private LabelProcessor labelProcessor;

    @Autowired
    private RedisService redisService;

    @Override
    public void processVO(ArticleVO article, String userId) {
        appendContentHtml(article);
        processThumbUps(article, userId);
//        List<CommentVO> comments = commentAppProcessor.processDisplayedComments(article.getArticleId());
//        commentAppProcessor.processCommentLikes(comments, userId);
//        article.setComments(comments);
        setCommentCount(article);
        labelProcessor.processAppDetailLabels(article);
    }

    private void setCommentCount(ArticleVO article) {
        Example example = new Example(UserComment.class);
        example.createCriteria()
                .andEqualTo("isDelete", (byte)0)
                .andEqualTo("articleId", article.getArticleId());
        article.setCommentCount(userCommentMapper.selectCountByExample(example));
    }

    private void appendContentHtml(ArticleVO article) {
        String cacheKey = String.format("slas:cmuop:article:contentHtml:%s", article.getArticleId());
        String contentHtml = redisService.getString(cacheKey);
        if(StringUtils.isEmpty(contentHtml)) {
            ArticleContent content = searchFromESService.getArticle(article.getArticleId());
            if(content != null) {
                contentHtml = content.getContentHtml();
                redisService.setString(cacheKey, contentHtml, 3L, TimeUnit.DAYS);
            }
        }
        article.setContentHtml(contentHtml);
    }

    private void processThumbUps(ArticleVO article, String userId) {
        List<UserVO> allUserlikes = userLikeMapper.getUserLikesForArticle(article.getArticleId());
        article.setLikeCount(allUserlikes.size());
        article.setIfLike(false);
        if (!CollectionUtils.isEmpty(allUserlikes)) {
            List<String> allLikeIds = allUserlikes.stream().map(UserVO::getUid).collect(Collectors.toList());
            article.setIfLike(allLikeIds.contains(userId));
            // fetch the latest 6 thumb ups
            allLikeIds = allLikeIds.stream().limit(6).collect(Collectors.toList());
            article.setLikeUsers(userAppProcessor.appendUserInfo(allLikeIds));
        }
    }

    private enum LikeType {
        like((byte) 1), unlike((byte) -1);
        private Byte typeValue;

        LikeType(Byte value) {
            typeValue = value;
        }
    }
}
