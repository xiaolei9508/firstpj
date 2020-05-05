package com.cmft.slas.cmuop.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.cmft.slas.cmuop.common.utils.HttpUtil;
import com.cmft.slas.cmuop.common.utils.SignVerifyUtil;
import com.cmft.slas.cmuop.dto.UserEntityDTO;
import com.cmft.slas.cmuop.entity.UserEntity;
import com.cmft.slas.cmuop.mapper.CmuopEntityMapper;
import com.cmft.slas.cmuop.mapper.UserEntityMapper;
import com.cmft.slas.cmuop.service.UserEntityService;
import com.cmft.slas.common.pojo.Page;
import com.cmft.slas.common.utils.BeanMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class UserEntityServiceImpl implements UserEntityService {

    private static final Logger logger = LoggerFactory.getLogger(UserEntityServiceImpl.class);

    @Autowired
    private UserEntityMapper userEntityMapper;

    @Autowired
    private CmuopEntityMapper cmuopEntityMapper;

    @Value("${nuc.app-key}")
    private String appKey;

    @Value("${nuc.secret-key}")
    private String secretKey;

    @Value("${nuc.base-url}")
    private String nucUrl;

    @Value("${nuc.callerModule}")
    private String callerModule;

    public int addUserEntity(UserEntityDTO userEntityDTO) {
        return userEntityMapper.insertSelective(BeanMapper.map(userEntityDTO, UserEntity.class));
    }

    @Override
    public int updateUserEntity(UserEntityDTO userEntityDTO) {
        return userEntityMapper.updateByPrimaryKeySelective(BeanMapper.map(userEntityDTO, UserEntity.class));
    }

    @Override
    public int deleteUserEntity(Long id) {

        return userEntityMapper.deleteByPrimaryKey(id);

    }

    @Override
    public UserEntityDTO queryById(Long id) {
        UserEntityDTO userEntityDTO = BeanMapper.copy(userEntityMapper.selectByPrimaryKey(id), UserEntityDTO.class);
        if (null != userEntityDTO) {
            String entityCode = userEntityDTO.getEntityCode();
            userEntityDTO.setEntityName(cmuopEntityMapper.queryEntityNameByEntityCode(entityCode));
        }
        return userEntityDTO;
    }

    @Override
    public long countByCondition(UserEntityDTO userEntityDTO) {
        return userEntityMapper.countByCondition(userEntityDTO);

    }

    @Override
    public List<UserEntityDTO> queryByCondition(UserEntityDTO userEntityDTO) {
        List<UserEntityDTO> userEntityDTOList =
            BeanMapper.mapList(userEntityMapper.queryByCondition(userEntityDTO), UserEntityDTO.class);
        return userEntityDTOList;
    }

    @Override
    public PageInfo<UserEntityDTO> queryByPage(UserEntityDTO userEntityDTO, Page page) {
        if (StringUtils.isBlank(page.getOrderBy())) {
            page.setOrderBy("user_entity_id DESC");
        }
        /*
         * pagehelper分页
         */
        PageHelper.startPage(page);

        List<UserEntityDTO> userEntityDTOList =
            BeanMapper.mapList(userEntityMapper.queryByCondition(userEntityDTO), UserEntityDTO.class);
        PageInfo<UserEntityDTO> pageInfo = new PageInfo<>(userEntityDTOList);

        return pageInfo;
    }

    @Override
    public List<UserEntityDTO> getUserEntityList(UserEntityDTO userEntityDTO) {
        List<UserEntityDTO> userEntityDTOList =
            BeanMapper.mapList(userEntityMapper.queryByCondition(userEntityDTO), UserEntityDTO.class);
        return userEntityDTOList;
    }

    @Override
    public String queryEntityCodeFromNuc(String accountId) {
        String s = null;
        Map bodyMap = new HashMap();
        String timestamp = System.currentTimeMillis() + "";
        bodyMap.put("account_id", accountId);
        String sign = SignVerifyUtil.generateSign(bodyMap, timestamp, appKey, secretKey);
        Map headerMap = new HashMap<>();
        headerMap.put("signature", sign);
        headerMap.put("timestamp", timestamp);
        headerMap.put("CallerModule", callerModule);
        headerMap.put("App-Key", appKey);
        try {
            s = HttpUtil.post(nucUrl.concat("/nuc-api/v1/getQuarterByAcc"), JSON.toJSONString(bodyMap), headerMap);
        } catch (Exception e) {
            logger.error("nuc interface is error: " + e.getMessage());
        }
        return s;
    }

}
