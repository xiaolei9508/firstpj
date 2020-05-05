package com.cmft.slas.cmuop.service.impl;

import com.cmft.slas.cmuop.dto.UserLikeCommentDTO;
import com.cmft.slas.cmuop.entity.UserLikeComment;
import com.cmft.slas.cmuop.mapper.UserLikeCommentMapper;
import com.cmft.slas.cmuop.service.UserLikeCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cmft.slas.cmuop.common.utils.BeanMapper;

import java.util.Date;
import java.util.List;

@Service("UserLikeCommentService")
public class UserLikeCommentServiceImpl implements UserLikeCommentService {

    @Autowired
    private UserLikeCommentMapper userLikeCommentMapper;

    @Autowired
    private BeanMapper beanMapper;

    @Override
    public Long countByCondition(UserLikeCommentDTO userLikeCommentDTO) {
        return userLikeCommentMapper.countByCondition(userLikeCommentDTO);

    }

    @Override
    public List<UserLikeCommentDTO> queryByCondition(UserLikeCommentDTO userLikeCommentDTO) {
        List<UserLikeCommentDTO> UserLikeCommentDTOList =
            beanMapper.mapList(userLikeCommentMapper.queryByCondition(userLikeCommentDTO), UserLikeCommentDTO.class);
        return UserLikeCommentDTOList;
    }

    @Override
    public List<UserLikeCommentDTO> queryByComment(Date previousFireTime, Date fireTime) {

        List<UserLikeCommentDTO> UserLikeCommentDTOList = beanMapper
            .mapList(userLikeCommentMapper.queryByComment(previousFireTime, fireTime), UserLikeCommentDTO.class);
        return UserLikeCommentDTOList;
    }

    @Override
    public boolean ifUserLikeComment(UserLikeCommentDTO userLikeCommentDTO) {
        Integer sum = userLikeCommentMapper.ifUserLikeComment(userLikeCommentDTO);
        return sum >= 1;
    }

    @Override
    public int likeThisArticleComment(UserLikeCommentDTO userLikeCommentDTO) {
        UserLikeComment userLikeComment = beanMapper.map(userLikeCommentDTO, UserLikeComment.class);
        int row = userLikeCommentMapper.insertSelective(userLikeComment);
        return row;
    }
}
