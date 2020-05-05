package com.cmft.slas.cmuop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cmft.slas.cmuop.dto.FieldDTO;
import com.cmft.slas.cmuop.service.FieldService;
import com.cmft.slas.common.pojo.Page;
import com.cmft.slas.common.pojo.WebResponse;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api("行业板块管理接口")
@RequestMapping(value = "v1/field")
public class FieldController {

    @Autowired
    private FieldService fieldService;

    // 根据id查询行业板块
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "根据id行业板块信息", notes = "根据id行业板块信息", produces = "application/json")
    public WebResponse<FieldDTO> queryFieldById(@PathVariable Long id) {
        WebResponse<FieldDTO> webResponse = new WebResponse<>();
        FieldDTO fieldDTO = fieldService.queryById(id);
        return WebResponse.success(fieldDTO);
    }

    // 获取行业板块分页
    @ApiOperation(value = "分页查询记录接口", notes = "分页查询记录接口", consumes = "application/x-www-form-urlencoded",
        produces = "application/json")
    @RequestMapping(value = "/fieldPage", method = RequestMethod.POST)
    public WebResponse<PageInfo<FieldDTO>> queryResourcePage(@ModelAttribute FieldDTO fieldDTO,
        @ModelAttribute Page page) throws Exception {
        WebResponse<PageInfo<FieldDTO>> webResponse = new WebResponse<>();
        PageInfo<FieldDTO> packageList = fieldService.queryByPage(fieldDTO, page);
        return webResponse.setBody(packageList);
    }

    // 获取行业板块列表(算法)
    @ApiOperation(value = "查询行业板块列表", notes = "查询行业板块列表", consumes = "application/json", produces = "application/json")
    @RequestMapping(value = "/fieldList", method = RequestMethod.GET)
    public WebResponse<List<FieldDTO>> queryFieldList() {
        List<FieldDTO> list = fieldService.queryByCondition(new FieldDTO());
        return WebResponse.success(list);
    }

    // 修改行业板块
    @ApiOperation(value = "修改行业板块", notes = "修改行业板块", consumes = "application/json", produces = "application/json")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public WebResponse updateField(@RequestBody FieldDTO fieldDTO) {
        int i = fieldService.updateField(fieldDTO);
        return i > 0 ? WebResponse.success("修改成功") : WebResponse.error("修改失败");
    }

    // 新增行业板块
    @ApiOperation(value = "新增行业板块", notes = "新增行业板块", consumes = "application/json", produces = "application/json")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public WebResponse addField(@RequestBody FieldDTO fieldDTO) {
        int i = fieldService.addField(fieldDTO);
        return i > 0 ? WebResponse.success("新增成功") : WebResponse.error("新增失败");
    }

    // 删除行业板块
    @ApiOperation(value = "删除行业板块", notes = "删除行业板块", consumes = "application/json", produces = "application/json")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public WebResponse deleteField(@PathVariable Long id) {
        int i = fieldService.deleteField(id);
        return i > 0 ? WebResponse.success("删除成功") : WebResponse.error("删除失败");
    }
}
