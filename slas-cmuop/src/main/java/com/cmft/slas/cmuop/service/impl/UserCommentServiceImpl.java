package com.cmft.slas.cmuop.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmft.slas.cmuop.common.utils.BeanMapper;
import com.cmft.slas.cmuop.dto.UserCommentDTO;
import com.cmft.slas.cmuop.entity.UserComment;
import com.cmft.slas.cmuop.mapper.UserCommentMapper;
import com.cmft.slas.cmuop.processor.processorImpl.CommentAppProcessor;
import com.cmft.slas.cmuop.service.UserCommentService;
import com.cmft.slas.cmuop.vo.CommentVO;
import com.cmft.slas.common.pojo.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service("userCommentService")
public class UserCommentServiceImpl implements UserCommentService {
    @Autowired
    private UserCommentMapper userCommentMapper;

    @Autowired
    private BeanMapper beanMapper;

    @Autowired
    private CommentAppProcessor commentAppProcessor;

    @Override
    public Long countByCondition(UserCommentDTO userCommentDTO) {
        return userCommentMapper.countByCondition(userCommentDTO);

    }

    @Override
    public List<UserCommentDTO> queryByCondition(UserCommentDTO userCommentDTO) {
        List<UserCommentDTO> userViewDTOList =
                beanMapper.mapList(userCommentMapper.queryByCondition(userCommentDTO), UserCommentDTO.class);
        return userViewDTOList;
    }

    @Override
    public List<UserCommentDTO> queryByArticle(Date previousFireTime, Date fireTime) {

        List<UserCommentDTO> UserCommentDTOList =
                beanMapper.mapList(userCommentMapper.queryByArticle(previousFireTime, fireTime), UserCommentDTO.class);
        return UserCommentDTOList;

    }

    @Override
    public Long addUserComment(UserCommentDTO userCommentDTO) {
        UserComment userComment = beanMapper.map(userCommentDTO, UserComment.class);
        userComment.setCommentId(null);
        userCommentMapper.insertSelective(userComment);
        return userComment.getCommentId();
    }

    @Override
    public int deleteUserComment(Long commentId) {
        UserComment userComment = new UserComment();
        userComment.setCommentId(commentId);
        userComment.setIsDelete((byte) 1);
        UserComment comment = userCommentMapper.selectByPrimaryKey(commentId);
        if (comment.getRootId() == 0) {
            UserComment deleteComment = new UserComment();
            deleteComment.setIsDelete((byte) 1);
            Example example = new Example(UserComment.class);
            Criteria criteria = example.createCriteria();
            criteria.andEqualTo("rootId", commentId);
            userCommentMapper.updateByExampleSelective(deleteComment, example);
        }
        return userCommentMapper.updateByPrimaryKeySelective(userComment);
    }

    @Override
    public PageInfo<CommentVO> getUserComments(Long articleId, String uid, Page page) {
        Example example = new Example(UserComment.class);
        example.createCriteria().andEqualTo("articleId", articleId).andEqualTo("rootId", 0).andEqualTo("isDelete",
                (byte) 0);
        PageHelper.startPage(page == null ? 1 : page.getPageNum(), page == null ? 10 : page.getPageSize());
        List<UserComment> userCommentList = userCommentMapper.selectByExample(example);
        PageInfo<UserComment> userCommentPageInfo = new PageInfo<>(userCommentList);
        PageInfo<CommentVO> pageInfo = new PageInfo<>();
        pageInfo.setList(commentAppProcessor.processRootComments(userCommentList));
        commentAppProcessor.processCommentLikes(pageInfo.getList(), uid);
        BeanUtils.copyProperties(userCommentPageInfo, pageInfo, "list");
        return pageInfo;
    }
}
