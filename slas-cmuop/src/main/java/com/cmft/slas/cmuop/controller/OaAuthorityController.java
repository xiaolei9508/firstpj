package com.cmft.slas.cmuop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cmft.slas.cmuop.common.constant.Constant;
import com.cmft.slas.cmuop.service.OaAuthorityService;
import com.cmft.slas.common.pojo.WebResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api("权限管理接口")
@RequestMapping(value = "v1/oaAuthority")
public class OaAuthorityController {

    @Autowired
    private OaAuthorityService oaAuthorityService;

    // 删除权限
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除权限", notes = "删除权限", produces = "application/json")
    public WebResponse<String> deleteOaAuthority(@PathVariable Long id) {
        int res = oaAuthorityService.deleteOaAuthority(id);
        return res > 0 ? WebResponse.success("删除成功") : WebResponse.error(Constant.N, "删除失败");
    }

    /**
     * 获取psId和oa账号和权限
     */
    @ApiOperation(value = "增加人员权限", notes = "增加人员权限", produces = "application/json")
    @RequestMapping(value = "/oaAuthority/{uid}", method = RequestMethod.GET)
    public void oaAuthority(@PathVariable String uid) {
        oaAuthorityService.addOaAuthority(uid);
    }

    /**
     * 同步nuc数据文件
     * 
     */
    @ApiOperation(value = "同步nuc数据文件", notes = "同步nuc数据文件", produces = "application/json")
    @RequestMapping(value = "/oaAuthority/synNucDataFile", method = RequestMethod.GET)
    public void synNucDataFile() {
        oaAuthorityService.synNucDataFile();
    }
}
