package com.cmft.slas.cmuop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cmft.slas.cmuop.common.constant.Constant;
import com.cmft.slas.cmuop.dto.UserEntityDTO;
import com.cmft.slas.cmuop.service.UserEntityService;
import com.cmft.slas.common.pojo.Page;
import com.cmft.slas.common.pojo.WebResponse;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api("用户可见组织管理接口")
@RequestMapping(value = "v1/userEntity")
public class UserEntityController {

    @Autowired
    private UserEntityService userEntityService;

    // 添加用户可见组织
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加用户可见组织", notes = "添加用户可见组织", produces = "application/json")
    public WebResponse addUserEntity(@RequestBody UserEntityDTO userEntityDTO) {
        int res = userEntityService.addUserEntity(userEntityDTO);
        return res > 0 ? WebResponse.success("新增成功") : WebResponse.error(Constant.N, "新增失败");
    }

    // 删除用户可见组织
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除用户可见组织", notes = "删除用户可见组织", produces = "application/json")
    public WebResponse deleteUserEntity(@PathVariable Long id) {
        int res = userEntityService.deleteUserEntity(id);
        return res > 0 ? WebResponse.success("删除成功") : WebResponse.error(Constant.N, "删除失败");
    }

    // 更新用户可见组织
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ApiOperation(value = "更新用户可见组织", notes = "更新用户可见组织", produces = "application/json")
    public WebResponse updateUserEntity(@RequestBody UserEntityDTO userEntityDTO) {
        int res = userEntityService.updateUserEntity(userEntityDTO);
        return res > 0 ? WebResponse.success("更新成功") : WebResponse.error(Constant.N, "更新失败");
    }

    // 根据id查询用户可见组织
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "根据id用户可见组织信息", notes = "根据id用户可见组织信息", produces = "application/json")
    public WebResponse<UserEntityDTO> queryUserEntityById(@PathVariable Long id) {
        WebResponse<UserEntityDTO> webResponse = new WebResponse<>();
        UserEntityDTO userEntityDTO = userEntityService.queryById(id);
        if (userEntityDTO == null) {
            webResponse.setCode(Constant.N);
        }
        webResponse.setBody(userEntityDTO);
        return webResponse;
    }

    // 获取用户可见组织分页
    @ApiOperation(value = "分页查询记录接口", notes = "分页查询记录接口", consumes = "application/x-www-form-urlencoded",
        produces = "application/json")
    @RequestMapping(value = "/userEntityPage", method = RequestMethod.POST)
    public WebResponse<PageInfo<UserEntityDTO>> queryResourcePage(@ModelAttribute UserEntityDTO userEntityDTO,
        @ModelAttribute Page page) throws Exception {
        WebResponse<PageInfo<UserEntityDTO>> webResponse = new WebResponse<>();
        PageInfo<UserEntityDTO> packageList = userEntityService.queryByPage(userEntityDTO, page);
        webResponse.setBody(packageList);
        return webResponse;
    }

    // 获取用户可见组织列表
    @ApiOperation(value = "查询用户可见组织列表", notes = "查询用户可见组织列表", consumes = "application/json",
        produces = "application/json")
    @RequestMapping(value = "/userEntityList", method = RequestMethod.POST)
    public WebResponse<List<UserEntityDTO>> queryUserEntityList(@RequestBody UserEntityDTO userEntityDTO) {
        WebResponse<List<UserEntityDTO>> webResponse = new WebResponse<>();
        List<UserEntityDTO> list = userEntityService.getUserEntityList(userEntityDTO);
        webResponse.setBody(list);
        return webResponse;
    }

    /**
     * 从nuc获取岗位信息
     * 
     * @param accountId
     * @throws Exception
     */
    @ApiOperation(value = "查询nuc中用户的entityCode", notes = "查询nuc中用户的entityCode", consumes = "application/json",
        produces = "application/json")
    @RequestMapping(value = "/entityCodeFromNuc/{accountId}", method = RequestMethod.GET)
    public WebResponse<String> queryEntityCodeFromNuc(@PathVariable String accountId) throws Exception {
        return WebResponse.success(userEntityService.queryEntityCodeFromNuc(accountId));
    }
}
