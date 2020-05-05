package com.cmft.slas.cmuop.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmft.slas.cmuop.common.utils.BeanMapper;
import com.cmft.slas.cmuop.dto.UserViewDTO;
import com.cmft.slas.cmuop.mapper.UserViewMapper;
import com.cmft.slas.cmuop.service.UserViewService;
import com.cmft.slas.cmuop.vo.UserViewVO;

@Service("userViewService")
public class UserViewServiceImpl implements UserViewService {
    @Autowired
    private UserViewMapper userViewMapper;
    @Autowired
    private BeanMapper beanMapper;

    @Override
    public Long countByCondition(UserViewDTO userViewDTO) {
        return userViewMapper.countByCondition(userViewDTO);

    }

    @Override
    public List<UserViewDTO> queryByCondition(UserViewDTO userViewDTO) {
        List<UserViewDTO> userViewDTOList =
                beanMapper.mapList(userViewMapper.queryByCondition(userViewDTO), UserViewDTO.class);
        return userViewDTOList;
    }

    @Override
    public List<UserViewDTO> queryByArticle(Date previousFireTime,Date fireTime) {

        List<UserViewDTO> userViewDTOList =
                beanMapper.mapList(userViewMapper.queryByArticle(previousFireTime,fireTime), UserViewDTO.class);
        return userViewDTOList;

    }

    @Override
    public List<UserViewVO> getUserView(String uid, String day) {
        return userViewMapper.getUserView(uid, day + " 00:00:00", day + " 23:59:59");
    }

}
