package com.cmft.slas.cmuop.service.impl;

import com.cmft.slas.cmuop.dto.UserDTO;
import com.cmft.slas.cmuop.entity.User;
import com.cmft.slas.cmuop.service.UserCheckService;
import com.cmft.slas.cmuop.service.UserService;
import com.cmft.slas.common.pojo.WebResponse;
import com.cmft.slas.common.utils.BeanMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

/**
 * @Author liurp001
 * @Since 2020/4/29
 */
@Service
@Slf4j
public class UserCheckServiceImpl implements UserCheckService {

    @Autowired
    private UserService userService;

    @Override
//    @Transactional
    public String checkUser(UserDTO userDTO) {
       try{
           Long t1 = System.currentTimeMillis();
           log.info("userCheck with user:{}:",userDTO.toString());
           String uid = userDTO.getUid();
           UserDTO countUserDTO = new UserDTO();
           countUserDTO.setUid(uid);
           //long count = userService.countByCondition(countUserDTO);
           Optional<UserDTO> userDTO1 = Optional.ofNullable(userService.queryById(Long.parseLong(uid)));
           if (userDTO1.isPresent()){
               if (StringUtils.isBlank(userDTO1.get().getName())){
                   User user = BeanMapper.map(userDTO1, User.class);
                   String result = userService.queryUserBusinessUnit(uid);
                   userService.insetUserBusinessUnit(user, result);
               }
               log.info("userCheck done cost [{}]ms when user exist:{}:",System.currentTimeMillis() - t1,userDTO.toString());
               return "用户uid存在";
           }
           int res = userService.addUser(userDTO);
           log.info("userCheck done [{}]ms when user not exist:{}:",System.currentTimeMillis() - t1, userDTO.toString());
           return res > 0 ? "新增用户成功" : "新增用户失败";
       }catch(Exception e){
           log.error(e.getMessage());
           return "新增用户失败";
       }
    }
}
