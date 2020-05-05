package com.cmft.slas.cmuop.service.impl;

import com.cmft.slas.cmuop.entity.User;
import com.cmft.slas.cmuop.entity.UserEntity;
import com.cmft.slas.cmuop.mapper.UserEntityMapper;
import com.cmft.slas.cmuop.mapper.UserMapper;
import com.cmft.slas.cmuop.processor.processorImpl.LeaderArticleProcessor;
import com.cmft.slas.cmuop.service.CmuopWebUserService;
import com.cmft.slas.cmuop.vo.LeaderPreview;
import com.google.common.collect.Lists;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CmuopWebUserServiceImpl implements CmuopWebUserService {
    @Autowired
    private UserEntityMapper userEntityMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LeaderArticleProcessor leaderArticleProcessor;

    @Override
    public List<LeaderPreview> getLeaderList(String entityCode) {
        List<LeaderPreview> res = Lists.newArrayList();
        Example example = new Example(UserEntity.class);
        example.createCriteria()
                .andEqualTo("entityCode", entityCode)
                .andEqualTo("isValid", (byte)1)
                .andEqualTo("isDelete", (byte)0);
        List<UserEntity> userEntities = userEntityMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(userEntities)){
            return res;
        }
        List<String> uids = userEntities.stream().map(UserEntity::getUid).collect(Collectors.toList());
        Example userEg = new Example(User.class);
        userEg.createCriteria().andIn("uid", uids);
        List<User> users = userMapper.selectByExample(userEg);
        if(CollectionUtils.isEmpty(users))
            return res;
        Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getUid, value -> value));
        userEntities.forEach(ue ->{
            LeaderPreview leaderPreview = new LeaderPreview();
            BeanUtils.copyProperties(ue, leaderPreview);
            leaderPreview.setUserName(userMap.get(ue.getUid()).getName());
            res.add(leaderPreview);
        });
        return res;
    }

    @Override
    public String updateLeaderOrder(String uid, String entityCode, Integer orderNum) {
        UserEntity userEntity = new UserEntity().setOrderNum(orderNum);
        Example example = new Example(UserEntity.class);
        example.createCriteria().andEqualTo("isValid", (byte)1).andEqualTo("isDelete", (byte)0);
        userEntityMapper.updateByExample(userEntity, example);
        leaderArticleProcessor.updateOrderNum(uid, entityCode);
        return "yes";
    }

    @Override
    public String updateEntity(String entityCode) {
        leaderArticleProcessor.updateOrderNum(null, entityCode);
        return "yes";
    }
}
