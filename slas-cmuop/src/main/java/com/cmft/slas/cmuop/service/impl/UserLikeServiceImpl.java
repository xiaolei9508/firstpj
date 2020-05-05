package com.cmft.slas.cmuop.service.impl;

import com.cmft.slas.cmuop.common.utils.BeanMapper;
import com.cmft.slas.cmuop.dto.UserLikeDTO;
import com.cmft.slas.cmuop.entity.UserLike;
import com.cmft.slas.cmuop.mapper.UserLikeMapper;
import com.cmft.slas.cmuop.service.UserLikeService;
import com.cmft.slas.cmuop.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("userLikeService")
public class UserLikeServiceImpl implements UserLikeService {
    @Autowired
    private UserLikeMapper userLikeMapper;
    @Autowired
    private BeanMapper beanMapper;

    @Override
    public Long countByCondition(UserLikeDTO userLikeDTO) {
        return userLikeMapper.countByCondition(userLikeDTO);

    }

    @Override
    public List<UserLikeDTO> queryByCondition(UserLikeDTO userLikeDTO) {
        List<UserLikeDTO> userLikeDTOList =
            beanMapper.mapList(userLikeMapper.queryByCondition(userLikeDTO), UserLikeDTO.class);
        return userLikeDTOList;
    }

    @Override
    public List<UserLikeDTO> queryByArticle(Date previousFireTime, Date fireTime) {

        List<UserLikeDTO> userLikeDTOList =
            beanMapper.mapList(userLikeMapper.queryByArticle(previousFireTime, fireTime), UserLikeDTO.class);
        return userLikeDTOList;

    }

    @Override
    public boolean ifUserLiked(UserLikeDTO userLikeDTO) {
        Integer sum = userLikeMapper.ifUserLiked(userLikeDTO);
        return sum >= 1;
    }

    @Override
    public int likeThisArticle(UserLikeDTO userLikeDTO) {
        UserLike userLike = com.cmft.slas.common.utils.BeanMapper.map(userLikeDTO, UserLike.class);
        int row = userLikeMapper.insertSelective(userLike);
        return row;
    }

    @Override
    public List<UserVO> getUserLikes(String articleId) {
        return userLikeMapper.getUserLikesForArticle(articleId);
    }

}
