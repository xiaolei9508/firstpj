package com.cmft.slas.cmuop.processor.processorImpl;

import com.cmft.slas.cmuop.entity.User;
import com.cmft.slas.cmuop.entity.UserComment;
import com.cmft.slas.cmuop.entity.UserLikeComment;
import com.cmft.slas.cmuop.mapper.UserCommentMapper;
import com.cmft.slas.cmuop.mapper.UserLikeCommentMapper;
import com.cmft.slas.cmuop.mapper.UserMapper;
import com.cmft.slas.cmuop.vo.CommentVO;
import com.cmft.slas.cmuop.vo.ReplyVO;
import com.cmft.slas.cmuop.vo.UserVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author liurp001
 * @Since 2020/1/6
 */
@Component
public class CommentAppProcessor {

    @Autowired
    private UserAppProcessor userAppProcessor;

    @Autowired
    private UserCommentMapper userCommentMapper;

    @Autowired
    private UserLikeCommentMapper userLikeCommentMapper;

    @Autowired
    private UserMapper userMapper;

    public List<CommentVO> processDisplayedComments(String articleId) {
        // fetch the latest 3 comments
        Example example = new Example(UserComment.class);
        example.createCriteria()
                .andEqualTo("articleId", articleId)
                .andEqualTo("rootId", 0)
                .andEqualTo("isDelete", false);
//        example.orderBy("commentId").desc();
        // keep only 3 original comments
        List<UserComment> originalComments = userCommentMapper.selectByExample(example)
                .stream()
                .limit(3)
                .collect(Collectors.toList());
        return CollectionUtils.isEmpty(originalComments) ? new ArrayList<>() : processRootComments(originalComments);
    }

    public List<CommentVO> processRootComments(List<UserComment> originalComments) {
        List<CommentVO> commentVOS = new ArrayList<>();
        originalComments.forEach(comment -> {
            CommentVO vo = new CommentVO();
            BeanUtils.copyProperties(comment, vo);
            commentVOS.add(vo);
        });

        List<String> userIds = commentVOS.stream().map(CommentVO::getUid).distinct().collect(Collectors.toList());
        if(CollectionUtils.isEmpty(userIds))
            return new ArrayList<>();
        List<UserVO> userInfo = userAppProcessor.appendUserInfo(userIds);
        if(CollectionUtils.isEmpty(userInfo))
            return new ArrayList<>();
        Map<String, UserVO> userVOMap = userInfo.stream().collect(Collectors.toMap(UserVO::getUid, value -> value));
        // result list
        List<CommentVO> parentComments = new ArrayList<>();
        commentVOS.forEach(item -> {
            UserVO user = userVOMap.get(item.getUid());
            if(user != null) {
                item.setUser(user);
                item.setLikeCount((userLikeCommentMapper.selectCount(new UserLikeComment().setCommentId(item.getCommentId()))));
                parentComments.add(item);
            }
        });
        processChildComments(parentComments);
        return parentComments;
    }

    private void processChildComments(List<CommentVO> rootComment) {
        Map<Long, CommentVO> rootMap = rootComment.stream().collect(Collectors.toMap(CommentVO::getCommentId, value -> value));
        Example childrenEg = new Example(UserComment.class);
        childrenEg.createCriteria().andIn("rootId", new ArrayList<>(rootMap.keySet())).andEqualTo("isDelete", (byte)0);
        List<UserComment> childrenList = userCommentMapper.selectByExample(childrenEg);
        Map<Long, List<UserComment>> childrenCommentMap = childrenList.stream().collect(Collectors.groupingBy(UserComment::getRootId));
        for (Long rootId : childrenCommentMap.keySet()) {
            List<ReplyVO> childrenVO = new ArrayList<>();
            List<UserComment> subComments = childrenCommentMap.get(rootId);
            Map<String, String> childrenMap = getUserMap(subComments.stream()
                    .map(UserComment::getUid)
                    .distinct()
                    .collect(Collectors.toList()));

            subComments.forEach(item -> childrenVO.add(new ReplyVO()
                                        .setCommentId(item.getCommentId())
                                        .setComment(item.getComment())
                                        .setUid(item.getUid())
                                        .setName(childrenMap.get(item.getUid()))
                                        .setReplyName(item.getReplyName())
                                        .setReplyId(item.getReplyId())
            ));
            rootMap.get(rootId).setChildren(childrenVO);
        }
    }

    private Map<String, String> getUserMap(List<String> userIds){
        Example userEg = new Example(User.class);
        userEg.createCriteria().andIn("uid", userIds);
        return userMapper.selectByExample(userEg)
                .stream()
                .collect(Collectors.toMap(User::getUid, User::getName));

    }

    public void processCommentLikes(List<CommentVO> commentVO, String userId) {
        commentVO.forEach(comment -> {
            List<UserLikeComment> likeList = userLikeCommentMapper.getAllLikesForComment(comment.getCommentId());
            List<String> likeUsers = likeList.stream().map(UserLikeComment::getUid).collect(Collectors.toList());
            comment.setIfLike(likeUsers.contains(userId));
            comment.setLikeCount(likeUsers.size());
        });
    }
}
