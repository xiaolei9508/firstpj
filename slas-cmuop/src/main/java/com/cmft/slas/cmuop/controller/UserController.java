package com.cmft.slas.cmuop.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cmft.slas.cmuop.dto.UserDTO;
import com.cmft.slas.cmuop.service.UserService;
import com.cmft.slas.common.pojo.Page;
import com.cmft.slas.common.pojo.WebResponse;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Slf4j
@RestController
@Api("用户管理接口")
@RequestMapping(value = "v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 添加用户
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加用户", notes = "添加用户", produces = "application/json")
    public WebResponse addUser(@RequestBody UserDTO userDTO) {
        String uid = userDTO.getUid();
        UserDTO countUserDTO = new UserDTO();
        countUserDTO.setUid(uid);
        long count = userService.countByCondition(countUserDTO);
        if (count > 0) {
            return WebResponse.error("用户uid重复，请修改后重新添加");
        }
        int res = userService.addUser(userDTO);
        return res > 0 ? WebResponse.success("新增成功") : WebResponse.error("新增失败");
    }

    // 删除用户
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除用户", notes = "删除用户", produces = "application/json")
    public WebResponse deleteUser(@PathVariable Long id) {
        int res = userService.deleteUser(id);
        return res > 0 ? WebResponse.success("删除成功") : WebResponse.error("删除失败");
    }

    // 更新用户
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ApiOperation(value = "更新用户", notes = "更新用户", produces = "application/json")
    public WebResponse updateUser(@RequestBody UserDTO userDTO) {
        int res = userService.updateUser(userDTO);
        return res > 0 ? WebResponse.success("更新成功") : WebResponse.error("更新失败");
    }

    // 根据id查询用户
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "根据id用户信息", notes = "根据id用户信息", produces = "application/json")
    public WebResponse queryUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.queryById(id);
        return userDTO == null ? WebResponse.error("查询失败") : WebResponse.success(userDTO);
    }

    // 获取用户分页
    @ApiOperation(value = "分页查询记录接口", notes = "分页查询记录接口", consumes = "application/x-www-form-urlencoded",
        produces = "application/json")
    @RequestMapping(value = "/userPage", method = RequestMethod.POST)
    public WebResponse<PageInfo<UserDTO>> queryResourcePage(@ModelAttribute UserDTO userDTO,
        @ModelAttribute Page page) {
        WebResponse<PageInfo<UserDTO>> webResponse = new WebResponse<>();
        PageInfo<UserDTO> packageList = userService.queryByPage(userDTO, page);
        webResponse.setBody(packageList);
        return webResponse;
    }

    // 获取用户列表
    @ApiOperation(value = "查询用户列表", notes = "查询用户列表", consumes = "application/json", produces = "application/json")
    @RequestMapping(value = "/userList", method = RequestMethod.GET)
    public WebResponse<List<UserDTO>> queryUserList() {
        WebResponse<List<UserDTO>> webResponse = new WebResponse<>();
        List<UserDTO> list = userService.getUserList();
        webResponse.setBody(list);
        return webResponse;
    }

    /**
     * 查询全部人员关系
     */
    @ApiOperation(value = "查询人员关系", notes = "查询人员关系", consumes = "application/json", produces = "application/json")
    @RequestMapping(value = "/userRelated", method = RequestMethod.GET)
    public WebResponse<List<UserDTO>> queryUserEntityRelated() {
        List<UserDTO> userDTOS = userService.queryUserEntityRelated();
        return WebResponse.success(userDTOS);
    }

    /**
     * 查询is_valid = 1的人员关系(算法)
     */
    @ApiOperation(value = "查询isValid=1的人员关系", notes = "查询isValid=1的人员关系", consumes = "application/json",
        produces = "application/json")
    @RequestMapping(value = "/userIsValidRelated", method = RequestMethod.GET)
    public WebResponse<List<UserDTO>> queryUserEntityRelatedByIsValid() {
        List<UserDTO> userDTOS = userService.queryUserEntityRelatedByIsValid();
        return WebResponse.success(userDTOS);
    }

    /**
     * 从nuc中查询用户信息
     */
    @ApiOperation(value = "查询人员信息", notes = "查询人员信息", produces = "application/json")
    @RequestMapping(value = "/userInfoFromNuc/{userName}", method = RequestMethod.GET)
    public WebResponse<String> getUserInfoFromNuc(@PathVariable String userName) {
        return WebResponse.success(userService.getUserInfoFromNuc(userName));
    }

    /**
     * 根据uid查询用户信息
     */
    @ApiOperation(value = "查询人员信息", notes = "查询人员信息", produces = "application/json")
    @RequestMapping(value = "/userInfo/{uid}", method = RequestMethod.GET)
    public WebResponse<UserDTO> getUserInfoByUid(@PathVariable String uid) {
        Long t1 = System.currentTimeMillis();
        log.info("query userInfo for {}",uid);
        UserDTO userDTO = userService.getUserInfoByUid(uid);
        log.info("query userInfo done cost:[{}]ms for {}",System.currentTimeMillis()-t1, uid);
        return WebResponse.success(userDTO);
    }

    /**
     * 同步t_user_info表中的数据
     */
    @ApiOperation(value = "同步t_user_info表中的数据", notes = "同步t_user_info表中的数据", produces = "application/json")
    @RequestMapping(value = "/synUserInfo", method = RequestMethod.GET)
    public WebResponse synUserInfo() {
        userService.synUserInfo();
        return WebResponse.success("同步成功");
    }

    // 更新用户
    @RequestMapping(value = "/updateUserByUid", method = RequestMethod.PUT)
    @ApiOperation(value = "更新用户", notes = "更新用户", produces = "application/json")
    public WebResponse updateUserByUid(@RequestBody UserDTO userDTO) {
        int res = userService.updateUserByUid(userDTO);
        return res > 0 ? WebResponse.success("更新成功") : WebResponse.error("更新失败");
    }
}
