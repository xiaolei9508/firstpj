package com.cmft.slas.cmuop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cmft.slas.cmuop.common.constant.Constant;
import com.cmft.slas.cmuop.dto.CmuopLabelDTO;
import com.cmft.slas.cmuop.service.CmuopLabelService;
import com.cmft.slas.common.pojo.Page;
import com.cmft.slas.common.pojo.WebResponse;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api("标签管理接口")
@RequestMapping(value = "v1/cmuopLabel")
public class CmuopLabelController {

    @Autowired
    private CmuopLabelService cmuopLabelService;

    // 添加标签
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加标签", notes = "添加标签", produces = "application/json")
    public WebResponse addCmuopLabel(@RequestBody CmuopLabelDTO cmuopLabelDTO) {
        CmuopLabelDTO countCmuopLabelDTOName = new CmuopLabelDTO();
        countCmuopLabelDTOName.setLabelName(cmuopLabelDTO.getLabelName());
        countCmuopLabelDTOName.setLabelType(cmuopLabelDTO.getLabelType());
        long countName = cmuopLabelService.countByCondition(countCmuopLabelDTOName);
        if (countName > 0) {
            return WebResponse.error("标签名称重复，请修改后重新添加");
        }
        CmuopLabelDTO countCmuopLabelDTOCode = new CmuopLabelDTO();
        countCmuopLabelDTOCode.setLabelCode(cmuopLabelDTO.getLabelCode());
        countCmuopLabelDTOCode.setLabelType(cmuopLabelDTO.getLabelType());
        long countCode = cmuopLabelService.countByCondition(countCmuopLabelDTOCode);
        if (countCode > 0) {
            return WebResponse.error("标签编码重复，请修改后重新添加");
        }
        int res = cmuopLabelService.addCmuopLabel(cmuopLabelDTO);
        return res > 0 ? WebResponse.success("新增成功") : WebResponse.error("新增失败");
    }

    // 删除标签
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除标签", notes = "删除标签", produces = "application/json")
    public WebResponse deleteCmuopLabel(@PathVariable Long id) {
        int res = cmuopLabelService.deleteCmuopLabel(id);
        return res > 0 ? WebResponse.success("删除成功") : WebResponse.error("删除失败");
    }

    // 更新标签
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ApiOperation(value = "更新标签", notes = "更新标签", produces = "application/json")
    public WebResponse updateCmuopLabel(@RequestBody CmuopLabelDTO cmuopLabelDTO) {
        CmuopLabelDTO cmuopLabelDTOOut = cmuopLabelService.queryByLabelId(cmuopLabelDTO.getLabelId());
        if (null != cmuopLabelDTOOut) {
            if (!cmuopLabelDTO.getLabelName().equals(cmuopLabelDTOOut.getLabelName())) {
                CmuopLabelDTO countCmuopLabelDTOName = new CmuopLabelDTO();
                countCmuopLabelDTOName.setLabelName(cmuopLabelDTO.getLabelName());
                countCmuopLabelDTOName.setLabelType(cmuopLabelDTO.getLabelType());
                long countName = cmuopLabelService.countByCondition(countCmuopLabelDTOName);
                if (countName > 0) {
                    return WebResponse.error("标签名称重复，请修改后重新添加");
                }
            }
            if (!cmuopLabelDTO.getLabelCode().equals(cmuopLabelDTOOut.getLabelCode())) {
                CmuopLabelDTO countCmuopLabelDTOCode = new CmuopLabelDTO();
                countCmuopLabelDTOCode.setLabelCode(cmuopLabelDTO.getLabelCode());
                countCmuopLabelDTOCode.setLabelType(cmuopLabelDTO.getLabelType());
                long countCode = cmuopLabelService.countByCondition(countCmuopLabelDTOCode);
                if (countCode > 0) {
                    return WebResponse.error("标签编码重复，请修改后重新添加");
                }
            }
        }
        int res = cmuopLabelService.updateCmuopLabel(cmuopLabelDTO);
        return res > 0 ? WebResponse.success("更新成功") : WebResponse.error("更新失败");
    }

    // 根据id查询标签
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "根据id标签信息", notes = "根据id标签信息", produces = "application/json")
    public WebResponse<CmuopLabelDTO> queryCmuopLabelById(@PathVariable Long id) {
        WebResponse<CmuopLabelDTO> webResponse = new WebResponse<>();
        CmuopLabelDTO cmuopLabelDTO = cmuopLabelService.queryCmuopLabelById(id);
        if (cmuopLabelDTO == null) {
            webResponse.setCode(Constant.N);
        }
        webResponse.setBody(cmuopLabelDTO);
        return webResponse;
    }

    // 获取标签分页
    @ApiOperation(value = "分页查询记录接口", notes = "分页查询记录接口", consumes = "application/x-www-form-urlencoded",
        produces = "application/json")
    @RequestMapping(value = "/cmuopLabelPage", method = RequestMethod.POST)
    public WebResponse<PageInfo<CmuopLabelDTO>> queryResourcePage(@ModelAttribute CmuopLabelDTO cmuopLabelDTO,
        @ModelAttribute Page page) throws Exception {
        WebResponse<PageInfo<CmuopLabelDTO>> webResponse = new WebResponse<>();
        PageInfo<CmuopLabelDTO> packageList = cmuopLabelService.queryByPage(cmuopLabelDTO, page);
        webResponse.setBody(packageList);
        return webResponse;
    }

    // 获取标签列表
    @ApiOperation(value = "查询标签列表", notes = "查询标签列表", consumes = "application/json", produces = "application/json")
    @RequestMapping(value = "/cmuopLabelList", method = RequestMethod.POST)
    public WebResponse<List<CmuopLabelDTO>> queryCmuopLabelList(@RequestBody CmuopLabelDTO cmuopLabelDTO) {
        WebResponse<List<CmuopLabelDTO>> webResponse = new WebResponse<>();
        List<CmuopLabelDTO> list = cmuopLabelService.queryByCondition(cmuopLabelDTO);
        webResponse.setBody(list);
        return webResponse;
    }

    // 查询标签列表(算法)
    @ApiOperation(value = "查询标签列表", notes = "查询标签列表", consumes = "application/json", produces = "application/json")
    @RequestMapping(value = "/cmuopLabelList/{labelType}", method = RequestMethod.GET)
    public WebResponse<List<CmuopLabelDTO>> queryCmuopLabelList(@PathVariable String labelType) {
        CmuopLabelDTO cmuopLabelDTO = new CmuopLabelDTO();
        cmuopLabelDTO.setLabelType(labelType);
        List<CmuopLabelDTO> cmuopLabelDTOList = cmuopLabelService.queryLabelListByCondition(cmuopLabelDTO);
        return WebResponse.success(cmuopLabelDTOList);
    }
}
