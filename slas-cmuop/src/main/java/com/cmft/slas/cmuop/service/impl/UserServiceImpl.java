package com.cmft.slas.cmuop.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmft.slas.cmuop.common.utils.Base64Util;
import com.cmft.slas.cmuop.common.utils.HttpUtil;
import com.cmft.slas.cmuop.common.utils.SignVerifyUtil;
import com.cmft.slas.cmuop.dto.CmuopEntityDTO;
import com.cmft.slas.cmuop.dto.UserDTO;
import com.cmft.slas.cmuop.dto.UserEntityDTO;
import com.cmft.slas.cmuop.entity.CmuopEntity;
import com.cmft.slas.cmuop.entity.User;
import com.cmft.slas.cmuop.entity.UserEntity;
import com.cmft.slas.cmuop.mapper.CmuopEntityMapper;
import com.cmft.slas.cmuop.mapper.UserEntityMapper;
import com.cmft.slas.cmuop.mapper.UserMapper;
import com.cmft.slas.cmuop.service.CmuopFileService;
import com.cmft.slas.cmuop.service.UserService;
import com.cmft.slas.common.pojo.Page;
import com.cmft.slas.common.utils.BeanMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserEntityMapper entityMapper;

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

    @Value("${cmuop.img_path}")
    private String imgPrefix;

    @Value("${cmuop.avatar.default}")
    private String defaultImg;

    @Autowired
    private CmuopEntityServiceImpl cmuopEntityService;

    @Autowired
    private OaAuthorityServiceImpl oaAuthorityService;

    @Autowired
    private CmuopFileService cmuopFileService;

    @Override
    public int addUser(UserDTO userDTO) {
        String defaultImgUrl = imgPrefix.concat(defaultImg);
        if (null == userDTO.getIsValid()) {
            userDTO.setIsValid((byte)0);
        }
        String imgBase64 = userDTO.getImgBase64();
        // 查询对应的accountId和emplId
        Map<String, String> map = getAccountIdByUid(userDTO.getUid());
        if (StringUtils.isNotBlank(imgBase64)) {
            /**
             * 存储用户头像
             */
            MultipartFile multipartFile = Base64Util.base64ToMultipartFile(imgBase64);
            String imgPath = cmuopFileService.uploadAvatar(multipartFile, null, userDTO.getUid());
            userDTO.setImgUrl(imgPath);
        } else {
            /**
             * 通过t_user_info表进行查询用户对应的头像
             */
            // 查询uid对应的emplId
            String emplId = map.get("emplId");
            String businessUnit = map.get("businessUnit");
            userDTO.setImgUrl(defaultImgUrl);
            if (StringUtils.isNotBlank(emplId)) {
                if (StringUtils.isNotBlank(businessUnit)) {
                    emplId = businessUnit.concat(".").concat(emplId);
                }
                List<User> userList = userMapper.queryUserInfoByUserId(emplId);
                if (CollectionUtils.isNotEmpty(userList)) {
                    User user = userList.get(0);
                    String imgUrl = user.getImgUrl();
                    if (StringUtils.isNotBlank(imgUrl)) {
                        String[] split = imgUrl.split("/");
                        if (split.length > 1) {
                            imgUrl = imgPrefix.concat(split[split.length - 1]);
                            userDTO.setImgUrl(imgUrl);
                        }
                    }
                }

            }
        }
        boolean flag = true;
        User user = BeanMapper.map(userDTO, User.class);
        try {
            userMapper.insertSelective(user);
        } catch (Exception e) {
            flag = false;
            logger.error("用户插入报错：" + ExceptionUtils.getStackTrace(e));
        }
        if (!flag) {
            return 0;
        }
        String result = queryUserBusinessUnit(map.get("accountId"));
        insetUserBusinessUnit(user, result);
        // return oaAuthorityService.addOaAuthority(userDTO.getUid());
        return 1;
    }

    public int updateUser(UserDTO userDTO) {
        return userMapper.updateByPrimaryKeySelective(BeanMapper.map(userDTO, User.class));
    }

    @Override
    public int deleteUser(Long id) {
        return userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public UserDTO queryById(Long id) {
        return BeanMapper.copy(userMapper.selectByPrimaryKey(id), UserDTO.class);
    }

    @Override
    public long countByCondition(UserDTO userDTO) {
        return userMapper.countByCondition(userDTO);

    }

    @Override
    public List<UserDTO> queryByCondition(UserDTO userDTO) {
        return BeanMapper.mapList(userMapper.queryByCondition(userDTO), UserDTO.class);
    }

    @Override
    public PageInfo<UserDTO> queryByPage(UserDTO userDTO, Page page) {
        if (StringUtils.isBlank(page.getOrderBy())) {
            page.setOrderBy("user_id DESC");
        }
        /*
         * pagehelper分页
         */
        PageHelper.startPage(page);
        List<UserDTO> userDTOList = BeanMapper.mapList(userMapper.queryByCondition(userDTO), UserDTO.class);

        return new PageInfo<>(userDTOList);
    }

    @Override
    public List<UserDTO> getUserList() {
        List<UserDTO> userDTOList = BeanMapper.mapList(userMapper.queryUserList(), UserDTO.class);
        for (UserDTO userDTO : userDTOList) {
            UserEntityDTO userEntityDTO = new UserEntityDTO();
            userEntityDTO.setUid(userDTO.getUid());
            userDTO.setEntrepreneurList(getUserEntity(userEntityDTO));
        }
        return userDTOList;
    }

    @Override
    public List<UserDTO> queryUserEntityRelated() {
        List<UserDTO> userDTOList = BeanMapper.mapList(userMapper.queryUserList(), UserDTO.class);
        for (UserDTO userDTO : userDTOList) {
            UserEntityDTO userEntityDTO = new UserEntityDTO();
            userEntityDTO.setUid(userDTO.getUid());
            userDTO.setEntrepreneurList(getUserEntity(userEntityDTO));
        }
        return userDTOList;
    }

    @Override
    public List<UserDTO> queryUserEntityRelatedByIsValid() {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("isValid", 1).andEqualTo("isDelete", 0);
        List<UserDTO> userDTOS = BeanMapper.mapList(userMapper.selectByExample(example), UserDTO.class);
        for (UserDTO urDTO : userDTOS) {
            UserEntityDTO userEntityDTO = new UserEntityDTO();
            userEntityDTO.setUid(urDTO.getUid());
            userEntityDTO.setIsValid((byte)1);
            urDTO.setEntrepreneurList(getUserEntity(userEntityDTO));
        }
        return userDTOS;
    }

    @Override
    public void synUserInfo() {
        List<User> userInfoList = userMapper.queryUserInfo();
        // 查询用户对应的域账号
        userInfoList.forEach(user -> {
            String uid = user.getUid();
            String imgUrl = user.getImgUrl();
            String emplId = null;
            String businessUnit = null;
            String accountId = null;
            if (StringUtils.isNotBlank(uid)) {
                String[] split = uid.split("\\.");
                if (split.length > 1) {
                    businessUnit = split[0];
                    emplId = split[1];
                    String s = null;
                    Map bodyMap = new HashMap();
                    bodyMap.put("business_unit", businessUnit);
                    bodyMap.put("empl_id", emplId);
                    try {
                        s = HttpUtil.post(nucUrl.concat("/nuc-api/v1/getStaffInfo"), JSON.toJSONString(bodyMap),
                            requestParam(bodyMap));
                        if (StringUtils.isNotBlank(s)) {
                            JSONObject jsonObject = JSON.parseObject(s);
                            String status = jsonObject.getString("status");
                            if ("1".equals(status)) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                JSONObject paasUcStaff = data.getJSONObject("paasUcStaff");
                                if (null != paasUcStaff) {
                                    accountId = paasUcStaff.getString("account_id");
                                }
                            }
                        }
                    } catch (Exception e) {
                        logger.error("nuc interface is error: " + e.getMessage());
                    }
                } else {
                    accountId = uid;
                }

                logger.info("通过user_id{}查询到的accunt_id为{}", uid, accountId);
                // 根据accountId 查询 uid
                if (null != accountId) {
                    String s = null;
                    Map bodyMap = new HashMap();
                    bodyMap.put("account_id", accountId);
                    bodyMap.put("expansionProp", true);
                    try {
                        s = HttpUtil.post(nucUrl.concat("/nuc-api/v1/getAccountInfo"), JSON.toJSONString(bodyMap),
                            requestParam(bodyMap));
                        if (StringUtils.isNotBlank(s)) {
                            JSONObject jsonObject = JSON.parseObject(s);
                            String status = jsonObject.getString("status");
                            if ("1".equals(status)) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                if (null != data) {
                                    String trueUid = data.getString("uid");
                                    logger.info("通过accunt_id{}查询到的uid为{}", accountId, trueUid);
                                    if (StringUtils.isNotBlank(trueUid)) {
                                        Example example = new Example(User.class);
                                        example.createCriteria().andEqualTo("uid", trueUid);
                                        List<User> users = userMapper.selectByExample(example);
                                        if (CollectionUtils.isNotEmpty(users)) {
                                            // user表中存在修改
                                            User user1 = users.get(0);
                                            String[] split1 = imgUrl.split("/");
                                            if (split1.length > 0) {
                                                imgUrl = imgPrefix.concat(split1[split1.length - 1]);
                                                User user2 = new User();
                                                user2.setUserId(user1.getUserId());
                                                user2.setImgUrl(imgUrl);
                                                userMapper.updateByPrimaryKeySelective(user2);
                                            }
                                        } else {
                                            // 不存在则插入user表中
                                            String[] split1 = imgUrl.split("/");
                                            if (split1.length > 0) {
                                                imgUrl = imgPrefix.concat(split1[split1.length - 1]);
                                            }
                                            User user1 = new User();
                                            user1.setIsValid((byte)0);
                                            user1.setUid(trueUid);
                                            user1.setImgUrl(imgUrl);
                                            boolean flag = true;
                                            try {
                                                userMapper.insertSelective(user1);
                                            } catch (Exception e) {
                                                flag = false;
                                                logger.error("用户插入报错：" + ExceptionUtils.getStackTrace(e));
                                            }
                                            String result = queryUserBusinessUnit(accountId);
                                            insetUserBusinessUnit(user1, result);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        logger.error("nuc interface is error: " + e.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public int updateUserByUid(UserDTO userDTO) {
        if(userDTO == null || StringUtils.isBlank(userDTO.getUid()))
            return -1;
        User user = BeanMapper.map(userDTO, User.class);
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("uid", userDTO.getUid());
        return userMapper.updateByExampleSelective(user, example);
    }

    // userEntity数据封装
    private List<UserEntityDTO> getUserEntity(UserEntityDTO userEntityDTO) {
        List<UserEntityDTO> userEntityList =
            BeanMapper.mapList(entityMapper.queryByCondition(userEntityDTO), UserEntityDTO.class);
        for (UserEntityDTO userEntityDTO_01 : userEntityList) {
            CmuopEntityDTO cmuopEntityDTO = new CmuopEntityDTO();
            cmuopEntityDTO.setEntityCode(userEntityDTO_01.getEntityCode());
            List<CmuopEntity> cmuopEntityList = cmuopEntityMapper.queryByCondition(cmuopEntityDTO);
            if (CollectionUtils.isNotEmpty(cmuopEntityList)) {
                CmuopEntity cmuopEntity = cmuopEntityList.get(0);
                userEntityDTO_01.setIsValid(cmuopEntity.getIsValid());
                userEntityDTO_01.setEntityName(cmuopEntity.getEntityName());
            }
        }
        return userEntityList;
    }

    @Override
    public String getUserInfoFromNuc(String userName) {
        String s = null;
        Map bodyMap = new HashMap();
        bodyMap.put("name", userName);
        try {
            s = HttpUtil.post(nucUrl.concat("/nuc-api/v1/getUcUserList"), JSON.toJSONString(bodyMap),
                requestParam(bodyMap));
        } catch (Exception e) {
            logger.error("nuc interface is error: " + e.getMessage());
        }
        return s;
    }

    // 通过uid查询account_id
    private Map getAccountIdByUid(String uid) {
        HashMap<Object, Object> map = new HashMap<>();
        String accountId = null;
        String s = null;
        Map bodyMap = new HashMap();
        bodyMap.put("uid", uid);
        try {
            s = HttpUtil.post(nucUrl.concat("/nuc-api/v1/listStaffByUid"), JSON.toJSONString(bodyMap),
                requestParam(bodyMap));
            if (StringUtils.isNotBlank(s) && !s.equals(HttpStatus.SC_UNAUTHORIZED)) {
                JSONObject jsonObject = JSON.parseObject(s);
                String status = jsonObject.getString("status");
                if ("1".equals(status)) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    if (null != data && data.getJSONObject("account") != null) {
                        JSONObject account = data.getJSONObject("account");
                        accountId = account.getString("account_id");
                        map.put("accountId", accountId);
                        JSONArray staffList = data.getJSONArray("staffList");
                        if (null != staffList && staffList.size() > 0) {
                            String emplId = staffList.getJSONObject(0).getString("empl_id");
                            String businessUnit = staffList.getJSONObject(0).getString("business_unit");
                            map.put("emplId", emplId);
                            map.put("businessUnit", businessUnit);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("nuc interface url exception: " + ExceptionUtils.getStackTrace(e));
        }
        logger.info("通过uid{}查询到的accountId为{}", uid, accountId);
        return map;
    }

    // 查询用户对应的组织
    public String queryUserBusinessUnit(String accountId) {
        String s = null;
        if (StringUtils.isNotBlank(accountId)) {
            Map bodyMap = new HashMap();
            bodyMap.put("account_id", accountId);
            try {
                s = HttpUtil.post(nucUrl.concat("/nuc-api/v1/getQuarterByAcc"), JSON.toJSONString(bodyMap),
                    requestParam(bodyMap));
                logger.info("通过域账号：" + accountId + "，查询用户组织结果：" + s);
            } catch (Exception e) {
                logger.error("nuc interface is error: " + ExceptionUtils.getStackTrace(e));
            }
        }
        return s;
    }

    // 插入user表和user_entity表中
    public void insetUserBusinessUnit(User user, String businessUnitString) {
        if (StringUtils.isNotBlank(businessUnitString) && !businessUnitString.equals(HttpStatus.SC_UNAUTHORIZED)) {
            JSONObject jsonObject = JSON.parseObject(businessUnitString);
            String status = jsonObject.getString("status");
            if ("1".equals(status)) {
                JSONObject data = jsonObject.getJSONObject("data");
                if (null != data) {
                    JSONObject paasUcStaff = data.getJSONObject("paasUcStaff");
                    String emplName = paasUcStaff.getString("empl_name");
                    if (StringUtils.isBlank(user.getName())) {
                        user.setName(emplName);
                        Example example = new Example(User.class);
                        example.createCriteria().andEqualTo("userId", user.getUserId());
                        userMapper.updateByExampleSelective(user, example);
                    }
                    JSONArray staffList = data.getJSONArray("paasUcOrganizeQuartersStaff");
                    if (null != staffList && staffList.size() > 0) {
                        for (int i = 0; i < staffList.size(); i++) {
                            JSONObject staffListJSONObject = staffList.getJSONObject(i);
                            String businessUnit = staffListJSONObject.getString("business_unit");
                            String businessName = staffListJSONObject.getString("business_name");
                            String quartersDesc = staffListJSONObject.getString("quarters_desc");
                            // 核对实体
                            Example example = new Example(CmuopEntity.class);
                            example.createCriteria().andEqualTo("entityCodeNuc", businessUnit);
                            List<CmuopEntity> cmuopEntityList = cmuopEntityMapper.selectByExample(example);
                            String entityCode = businessUnit;
                            if (CollectionUtils.isNotEmpty(cmuopEntityList)) {
                                CmuopEntity cmuopEntity = cmuopEntityList.get(0);
                                entityCode = cmuopEntity.getEntityCode();
                            } else {
                                CmuopEntity cmuopEntity = new CmuopEntity();
                                cmuopEntity.setEntityCode(businessUnit);
                                cmuopEntity.setEntityName(businessName);
                                cmuopEntity.setEntityCodeNuc(businessUnit);
                                cmuopEntity.setParentCode(getBusinessUnitInfo(businessUnit));
                                cmuopEntity.setFromNuc((byte)1);
                                cmuopEntity.setIsValid((byte)0);
                                cmuopEntityMapper.insertSelective(cmuopEntity);
                                cmuopEntityService.cleanEntityData();
                            }
                            UserEntity userEntity = new UserEntity();
                            userEntity.setUid(user.getUid());
                            userEntity.setEntityCode(entityCode);
                            long countUserEntity =
                                userEntityMapper.countByCondition(BeanMapper.map(userEntity, UserEntityDTO.class));
                            if (countUserEntity <= 0) {
                                userEntity.setEntrepreneurPosition(quartersDesc);
                                userEntity.setFromNuc((byte)1);
                                userEntity.setIsValid((byte)0);
                                userEntityMapper.insertSelective(userEntity);
                            }
                        }
                    }
                } else {
                    logger.info("通过uid查询用户的信息，data数据为空,uid is：" + user.getUid());
                }
            } else {
                logger.error("通过uid未查询到人员信息,uid is：" + user.getUid());
            }
        } else {
            logger.error("查询用户组织为空,uid is：" + user.getUid());
        }
    }

    // 查询父组织编码
    private String getBusinessUnitInfo(String businessUnitIn) {
        String businessUnitOut = null;
        String s = null;
        Map bodyMap = new HashMap();
        bodyMap.put("business_unit", businessUnitIn);
        bodyMap.put("expansionProp", true);
        try {
            s = HttpUtil.post(nucUrl.concat("/nuc-api/v1/getBusinessUnitInfo"), JSON.toJSONString(bodyMap),
                requestParam(bodyMap));
        } catch (Exception e) {
            logger.error("nuc interface is error: " + e.getMessage());
        }
        if (StringUtils.isNotBlank(s)) {
            JSONObject jsonObject = JSON.parseObject(s);
            String status = jsonObject.getString("status");
            if ("1".equals(status)) {
                JSONObject data = jsonObject.getJSONObject("data");
                if (null != data) {
                    businessUnitOut = data.getString("parent_business_unit");
                }
            }
        }
        return businessUnitOut;
    }

    @Override
    public boolean isValidUser(String uid) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUid(uid);
        userDTO.setIsDelete((byte)0);
        return countByCondition(userDTO) == 1L;
    }

    @Override
    public UserDTO getUserInfoByUid(String uid) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUid(uid);
        List<User> userList = userMapper.queryByCondition(userDTO);
        if (CollectionUtils.isNotEmpty(userList)) {
            return BeanMapper.map(userList.get(0), UserDTO.class);
        }
        return null;
    }

    @Override
    public List<String> queryUserUidList() {
        return userMapper.queryUserUidList();
    }

    // bodyMap数据封装
    private Map requestParam(Map<String, Object> bodyMap) {
        String timestamp = System.currentTimeMillis() + "";
        String sign = SignVerifyUtil.generateSign(bodyMap, timestamp, appKey, secretKey);
        Map headerMap = new HashMap<>();
        headerMap.put("signature", sign);
        headerMap.put("timestamp", timestamp);
        headerMap.put("CallerModule", callerModule);
        headerMap.put("App-Key", appKey);
        return headerMap;
    }
}
