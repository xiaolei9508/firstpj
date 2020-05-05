package com.cmft.slas.cmuop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cmft.slas.cmuop.dto.EntityRelatedDTO;
import com.cmft.slas.cmuop.service.EntityRelatedService;
import com.cmft.slas.common.pojo.Page;
import com.cmft.slas.common.pojo.WebResponse;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api("实体关系")
@RequestMapping(value = "v1/entityRealted")
public class EntityRealtedController {

    @Autowired
    private EntityRelatedService entityRelatedService;

    // 添加实体关系
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加实体关系", notes = "添加实体关系", produces = "application/json")
    public WebResponse addEntityRelated(@RequestBody EntityRelatedDTO entityRelatedDTO) {
        EntityRelatedDTO countEntityRelatedDTO = new EntityRelatedDTO();
        countEntityRelatedDTO.setEntityCode(entityRelatedDTO.getEntityCode());
        countEntityRelatedDTO.setEntityRelatedId(entityRelatedDTO.getEntityRelatedId());
        Long count = entityRelatedService.countByCondition(countEntityRelatedDTO);
        if (count > 0) {
            return WebResponse.error("实体关系重复，请修改后重新添加");
        }
        int res = entityRelatedService.addEntityRelated(entityRelatedDTO);
        return res > 0 ? WebResponse.success("新增成功") : WebResponse.error("新增失败");

    }

    // 删除实体关系
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除实体关系", notes = "删除实体关系", produces = "application/json")
    public WebResponse deleteEntityRelated(@PathVariable Long id) {
        int i = entityRelatedService.deleteEntityRelated(id);
        return i > 0 ? WebResponse.success("删除成功") : WebResponse.error("删除失败");
    }

    // 更新实体关系
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ApiOperation(value = "更新实体关系", notes = "更新实体关系", produces = "application/json")
    public WebResponse updateUserEntity(@RequestBody EntityRelatedDTO entityRelatedDTO) {
        int res = entityRelatedService.updateEntityRelated(entityRelatedDTO);
        return res > 0 ? WebResponse.success("更新成功") : WebResponse.error("更新失败");
    }

    // 根据id查询实体关系
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "根据id获取实体关系", notes = "根据id用户可见组织信息", produces = "application/json")
    public WebResponse queryEntityRelatedById(@PathVariable Long id) {
        return WebResponse.success(entityRelatedService.queryEntityRelatedById(id));
    }

    // 获取实体关系分页
    @ApiOperation(value = "分页查询实体关系", notes = "分页查询实体关系", consumes = "application/x-www-form-urlencoded",
        produces = "application/json")
    @RequestMapping(value = "/entityRelatedPage", method = RequestMethod.POST)
    public WebResponse<PageInfo<EntityRelatedDTO>> queryEntityRelatedPage(
        @ModelAttribute EntityRelatedDTO entityRelatedDTO, @ModelAttribute Page page) throws Exception {
        PageInfo<EntityRelatedDTO> pageInfo = entityRelatedService.getEntityRelatedList(entityRelatedDTO, page);
        return WebResponse.success(pageInfo);
    }

    // 获取全部实体关系
    @ApiOperation(value = "查询实体关系", notes = "查询实体关系", consumes = "application/x-www-form-urlencoded",
        produces = "application/json")
    @RequestMapping(value = "/entityRelatedList", method = RequestMethod.POST)
    public WebResponse<List<EntityRelatedDTO>> queryEntityRelatedList(@RequestBody EntityRelatedDTO entityRelatedDTO) {
        return WebResponse.success(entityRelatedService.queryByCondition(entityRelatedDTO));
    }
}
