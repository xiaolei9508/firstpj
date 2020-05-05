package com.cmft.slas.cmuop.processor.processorImpl;

import com.cmft.slas.cmuop.entity.User;
import com.cmft.slas.cmuop.mapper.UserMapper;

import com.cmft.slas.cmuop.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
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
public class UserAppProcessor {
    @Autowired
    private UserMapper userMapper;

    public List<UserVO> appendUserInfo(List<String> allUserIds) {
        List<UserVO> userList = new ArrayList<>();
        for (String id : allUserIds)
            userList.add(new UserVO().setUid(id));
        Example userEg = new Example(User.class);
        userEg.createCriteria().andIn("uid", allUserIds);
        List<User> userlist = userMapper.selectByExample(userEg);
        if(CollectionUtils.isEmpty(userList))
            return userList;
        Map<String, User> userMap = userlist.stream().collect(Collectors.toMap(User::getUid, value -> value));
        List<UserVO> voList = new ArrayList<>();
        userList.forEach(userVO -> {
            User source = userMap.get(userVO.getUid());
            if (source != null) {
                BeanUtils.copyProperties(source, userVO);
                if (userVO.getImgUrl() == null)
                    userVO.setImgUrl("");
                voList.add(userVO);
            }
        });
        return voList;
    }
}
