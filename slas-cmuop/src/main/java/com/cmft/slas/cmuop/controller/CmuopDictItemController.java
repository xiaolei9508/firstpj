package com.cmft.slas.cmuop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cmft.slas.cmuop.dto.CmuopDictItemDTO;
import com.cmft.slas.cmuop.service.CmuopDictItemService;
import com.cmft.slas.common.pojo.Page;
import com.cmft.slas.common.pojo.WebResponse;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api("数据字典管理接口")
@RequestMapping(value = "v1/cmuopDictItem")
public class CmuopDictItemController {

    @Autowired
    private CmuopDictItemService cmuopDictItemService;

    // 添加数据字典
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加数据字典", notes = "添加数据字典", produces = "application/json")
    public WebResponse addCmuopDictItem(@RequestBody CmuopDictItemDTO cmuopDictItemDTO) {
        CmuopDictItemDTO countCmuopDictItemDTO = new CmuopDictItemDTO();
        countCmuopDictItemDTO.setDictCode(cmuopDictItemDTO.getDictCode());
        countCmuopDictItemDTO.setValue(cmuopDictItemDTO.getValue());
        long count = cmuopDictItemService.countByCondition(countCmuopDictItemDTO);
        if (count > 0) {
            return WebResponse.error("数据重复，请修改后重新添加");
        }
        int res = cmuopDictItemService.addCmuopDictItem(cmuopDictItemDTO);
        return res > 0 ? WebResponse.success("新增成功") : WebResponse.error("新增失败");
    }

    // 删除数据字典
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除数据字典", notes = "删除数据字典", produces = "application/json")
    public WebResponse deleteCmuopDictItem(@PathVariable Long id) {
        int res = cmuopDictItemService.deleteCmuopDictItem(id);
        return res > 0 ? WebResponse.success("删除成功") : WebResponse.error("删除失败");
    }

    // 更新数据字典
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ApiOperation(value = "更新数据字典", notes = "更新数据字典", produces = "application/json")
    public WebResponse updateCmuopDictItem(@RequestBody CmuopDictItemDTO cmuopDictItemDTO) {
        int res = 0;
        CmuopDictItemDTO countCmuopDictItemDTO = new CmuopDictItemDTO();
        countCmuopDictItemDTO.setDictCode(cmuopDictItemDTO.getDictCode());
        countCmuopDictItemDTO.setValue(cmuopDictItemDTO.getValue());
        long count = cmuopDictItemService.countByCondition(countCmuopDictItemDTO);
        if (count <= 0) {
            res = cmuopDictItemService.updateCmuopDictItem(cmuopDictItemDTO);
        }
        return res > 0 ? WebResponse.error("更新成功") : WebResponse.error("更新失败");
    }

    // 根据dictCode查询数据字典(算法)
    @RequestMapping(value = "/info/{dictCode}", method = RequestMethod.GET)
    @ApiOperation(value = "根据dictCode查询字典信息", notes = "根据dictCode查询字典信息", produces = "application/json")
    public WebResponse<List<CmuopDictItemDTO>> queryCmuopDictItemByDictCode(@PathVariable("dictCode") String dictCode) {
        CmuopDictItemDTO DictItemDTO = new CmuopDictItemDTO();
        DictItemDTO.setDictCode(String.valueOf(dictCode));
        List<CmuopDictItemDTO> cmuopDictItemDTOList = cmuopDictItemService.queryByConditionByDictCode(DictItemDTO);
        return WebResponse.success(cmuopDictItemDTOList);
    }

    // 获取数据字典分页
    @ApiOperation(value = "分页查询记录接口", notes = "分页查询记录接口", consumes = "application/x-www-form-urlencoded",
        produces = "application/json")
    @RequestMapping(value = "/cmuopDictItemPage", method = RequestMethod.POST)
    public WebResponse<PageInfo<CmuopDictItemDTO>> queryResourcePage(@ModelAttribute CmuopDictItemDTO cmuopDictItemDTO,
        @ModelAttribute Page page) throws Exception {
        WebResponse<PageInfo<CmuopDictItemDTO>> webResponse = new WebResponse<>();
        PageInfo<CmuopDictItemDTO> packageList = cmuopDictItemService.queryByPage(cmuopDictItemDTO, page);
        webResponse.setBody(packageList);
        return webResponse;
    }

    // 获取数据字典列表
    @ApiOperation(value = "查询数据字典列表", notes = "查询数据字典列表", consumes = "application/json", produces = "application/json")
    @RequestMapping(value = "/cmuopDictItemList", method = RequestMethod.GET)
    public WebResponse<List<CmuopDictItemDTO>> queryCmuopDictItemList() {
        WebResponse<List<CmuopDictItemDTO>> webResponse = new WebResponse<>();
        CmuopDictItemDTO cmuopDictItemDTO = new CmuopDictItemDTO();
        List<CmuopDictItemDTO> list = cmuopDictItemService.queryByCondition(cmuopDictItemDTO);
        webResponse.setBody(list);
        return webResponse;
    }

    // 根据字典编码清除数据字典缓存
    @ApiOperation(value = "根据字典编码清除数据字典缓存", notes = "根据字典编码清除数据字典缓存", consumes = "application/json",
        produces = "application/json")
    @RequestMapping(value = "/cleanDictItemByDictCode/{dictCode}", method = RequestMethod.GET)
    public WebResponse cleanDictItemByDictCode(@PathVariable String dictCode) {
        Boolean b = cmuopDictItemService.cleanDictItemByDictCode(dictCode);
        if (b) {
            return WebResponse.success("清除成功");
        } else {
            return WebResponse.error("清除失败");
        }
    }

    // 传入rediskey清除缓存数据
    @ApiOperation(value = "传入rediskey清除缓存数据", notes = "传入rediskey清除缓存数据", consumes = "application/json",
        produces = "application/json")
    @RequestMapping(value = "/cleanRedis/{redisKey}", method = RequestMethod.GET)
    public WebResponse cleanRedisDataByRedisKey(@PathVariable String redisKey) {
        return WebResponse.success(cmuopDictItemService.cleanRedisDataByRedisKey(redisKey));
    }
}
