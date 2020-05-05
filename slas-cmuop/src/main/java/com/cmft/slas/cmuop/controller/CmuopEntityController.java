package com.cmft.slas.cmuop.controller;

import java.util.List;

import com.cmft.slas.cmuop.vo.EntityTreeVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cmft.slas.cmuop.common.constant.Constant;
import com.cmft.slas.cmuop.dto.CmuopEntityDTO;
import com.cmft.slas.cmuop.service.CmuopEntityService;
import com.cmft.slas.common.pojo.Page;
import com.cmft.slas.common.pojo.WebResponse;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api("组织信息管理接口")
@RequestMapping(value = "v1/cmuopEntity")
public class CmuopEntityController {

    @Autowired
    private CmuopEntityService cmuopEntityService;

    // 添加组织信息
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加组织信息", notes = "添加组织信息", produces = "application/json")
    public WebResponse addCmuopEntity(@RequestBody CmuopEntityDTO cmuopEntityDTO) {
        CmuopEntityDTO countCmuopEntityCode = new CmuopEntityDTO();
        countCmuopEntityCode.setEntityCode(cmuopEntityDTO.getEntityCode());
        long countEntityCode = cmuopEntityService.countByCondition(countCmuopEntityCode);
        if (countEntityCode > 0) {
            return WebResponse.error("组织编码重复，请修改后重新添加");
        }
        CmuopEntityDTO countCmuopEntityName = new CmuopEntityDTO();
        countCmuopEntityName.setEntityName(cmuopEntityDTO.getEntityName());
        long countEntityName = cmuopEntityService.countByCondition(countCmuopEntityName);
        if (countEntityName > 0) {
            return WebResponse.error("组织名称重复，请修改后重新添加");
        }
        if (StringUtils.isNotBlank(cmuopEntityDTO.getEntityCodeNuc())) {
            CmuopEntityDTO countCmuopNucCode = new CmuopEntityDTO();
            countCmuopNucCode.setEntityCodeNuc(cmuopEntityDTO.getEntityCodeNuc());
            long countNucCode = cmuopEntityService.countByCondition(countCmuopNucCode);
            if (countNucCode > 0) {
                return WebResponse.error("nuc编码重复，请修改后重新添加");
            }
        }
        int res = cmuopEntityService.addCmuopEntity(cmuopEntityDTO);
        return res > 0 ? WebResponse.success("新增成功") : WebResponse.error(Constant.N, "新增失败");
    }

    // 删除组织信息
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除组织信息", notes = "删除组织信息", produces = "application/json")
    public WebResponse deleteCmuopEntity(@PathVariable Long id) {
        int res = cmuopEntityService.deleteCmuopEntity(id);
        return res > 0 ? WebResponse.success("删除成功") : WebResponse.error("删除失败");
    }

    // 更新组织信息
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ApiOperation(value = "更新组织信息", notes = "更新组织信息", produces = "application/json")
    public WebResponse updateCmuopEntity(@RequestBody CmuopEntityDTO cmuopEntityDTO) {
        CmuopEntityDTO cmuopEntityDTOOut = cmuopEntityService.queryById(cmuopEntityDTO.getEntityId());
        if (!cmuopEntityDTO.getEntityCode().equals(cmuopEntityDTOOut.getEntityCode())) {
            CmuopEntityDTO countCmuopEntityCode = new CmuopEntityDTO();
            countCmuopEntityCode.setEntityCode(cmuopEntityDTO.getEntityCode());
            long countEntityCode = cmuopEntityService.countByCondition(countCmuopEntityCode);
            if (countEntityCode > 0) {
                return WebResponse.error("组织编码重复，请修改后重新添加");
            }
        }
        if (!cmuopEntityDTO.getEntityName().equals(cmuopEntityDTOOut.getEntityName())) {
            CmuopEntityDTO countCmuopEntityName = new CmuopEntityDTO();
            countCmuopEntityName.setEntityName(cmuopEntityDTO.getEntityName());
            long countEntityName = cmuopEntityService.countByCondition(countCmuopEntityName);
            if (countEntityName > 0) {
                return WebResponse.error("组织名称重复，请修改后重新添加");
            }
        }
        if (StringUtils.isNotBlank(cmuopEntityDTO.getEntityCodeNuc())) {
            if (!cmuopEntityDTO.getEntityCodeNuc().equals(cmuopEntityDTOOut.getEntityCodeNuc())) {
                CmuopEntityDTO countCmuopNucCode = new CmuopEntityDTO();
                countCmuopNucCode.setEntityCodeNuc(cmuopEntityDTO.getEntityCodeNuc());
                long countNucCode = cmuopEntityService.countByCondition(countCmuopNucCode);
                if (countNucCode > 0) {
                    return WebResponse.error("nuc编码重复，请修改后重新添加");
                }
            }
        }
        int res = cmuopEntityService.updateCmuopEntity(cmuopEntityDTO);
        return res > 0 ? WebResponse.success("更新成功") : WebResponse.error("更新失败");
    }

    // 根据id查询组织信息
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "根据id组织信息信息", notes = "根据id组织信息信息", produces = "application/json")
    public WebResponse<CmuopEntityDTO> queryCmuopEntityById(@PathVariable Long id) {
        WebResponse<CmuopEntityDTO> webResponse = new WebResponse<>();
        CmuopEntityDTO cmuopEntityDTO = cmuopEntityService.queryById(id);
        if (cmuopEntityDTO == null) {
            webResponse.setCode(Constant.N);
        }
        webResponse.setBody(cmuopEntityDTO);
        return webResponse;
    }

    // 获取组织信息分页
    @ApiOperation(value = "分页查询记录接口", notes = "分页查询记录接口", consumes = "application/x-www-form-urlencoded",
        produces = "application/json")
    @RequestMapping(value = "/cmuopEntityPage", method = RequestMethod.POST)
    public WebResponse<PageInfo<CmuopEntityDTO>> queryResourcePage(@ModelAttribute CmuopEntityDTO cmuopEntityDTO,
        @ModelAttribute Page page) throws Exception {
        WebResponse<PageInfo<CmuopEntityDTO>> webResponse = new WebResponse<>();
        PageInfo<CmuopEntityDTO> packageList = cmuopEntityService.queryByPage(cmuopEntityDTO, page);
        webResponse.setBody(packageList);
        return webResponse;
    }

    /**
     * 查询ifCmuip = 1全部实体
     */
    @ApiOperation(value = "查询组织信息列表", notes = "查询组织信息列表", consumes = "application/json", produces = "application/json")
    @RequestMapping(value = "/cmuopEntityList", method = RequestMethod.GET)
    public WebResponse<List<CmuopEntityDTO>> queryCmuopEntityList() {
        return WebResponse.success(cmuopEntityService.getCmuopEntityList());
    }

    /**
     * 查询isValid = 1 全部实体(算法)
     */
    @ApiOperation(value = "查询isValid = 1 全部实体", notes = "查询isValid = 1 全部实体", consumes = "application/json",
        produces = "application/json")
    @RequestMapping(value = "/cmuopEntityIsValidList", method = RequestMethod.GET)
    public WebResponse<List<CmuopEntityDTO>> queryCmuopEntityIsValidList() {
        return WebResponse.success(cmuopEntityService.queryCmuopEntityIsValidList());
    }

    /**
     * 查询实体关系
     */
    @ApiOperation(value = "查询实体关系", notes = "查询实体关系", consumes = "application/json", produces = "application/json")
    @RequestMapping(value = "/cmuopEntityRelated", method = RequestMethod.GET)
    public WebResponse<List<CmuopEntityDTO>> queryEntityRelated() {
        return WebResponse.success(cmuopEntityService.getCmuopEntityRelatedList());
    }

    /**
     * 查询isValid = 1 实体关系(算法)
     */
    @ApiOperation(value = "查询isValid = 1 实体关系", notes = "查询isValid = 1 实体关系", consumes = "application/json",
        produces = "application/json")
    @RequestMapping(value = "/cmuopEntityIsValidRelated", method = RequestMethod.GET)
    public WebResponse<List<CmuopEntityDTO>> queryEntityRelatedIsValid() {
        return WebResponse.success(cmuopEntityService.queryEntityRelatedIsValid());
    }

    /**
     * 从nuc查询组织编码
     */
    @ApiOperation(value = "查询组织编码", notes = "查询组织编码", consumes = "application/json", produces = "application/json")
    @RequestMapping(value = "/cmuopEntity/{entityName}", method = RequestMethod.GET)
    public WebResponse<List<String>> queryEntityCodeByNuc(@PathVariable String entityName) {
        return WebResponse.success(cmuopEntityService.queryEntityCodeByNuc(entityName));
    }

    /**
     * 重载redis RELATED_ENTITY_MAP缓存
     */
    @ApiOperation(value = "重载redis RELATED_ENTITY_MAP缓存", notes = "重载redis RELATED_ENTITY_MAP缓存",
        consumes = "application/json", produces = "application/json")
    @RequestMapping(value = "/cmuopEntity/reloadRem", method = RequestMethod.PUT)
    public WebResponse<String> reloadRem() {
        cmuopEntityService.getRelatedEntityMap(true);
        return WebResponse.success("更新redis RELATED_ENTITY_MAP缓存成功");
    }

    @RequestMapping(value = "/entityTree/{entityCode}")
    public WebResponse<EntityTreeVo> queryEntityTree(@PathVariable String entityCode) {
        if(StringUtils.isBlank(entityCode)) {
            entityCode = "001";
        }
        return WebResponse.success(cmuopEntityService.queryEntityTree(entityCode));
    }
}
