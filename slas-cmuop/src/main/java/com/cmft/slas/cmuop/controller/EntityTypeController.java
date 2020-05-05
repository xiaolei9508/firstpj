package com.cmft.slas.cmuop.controller;

import com.cmft.slas.cmuop.dto.CmuopEntityTypeDTO;
import com.cmft.slas.cmuop.dto.CmuopEntityTypeVO;
import com.cmft.slas.cmuop.dto.CmuopEntityTypeWithPointInfoTypeVO;
import com.cmft.slas.cmuop.service.EntityTypeService;
import com.cmft.slas.common.pojo.WebResponse;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 实体对应的新闻栏目
 */
@RestController
@RequestMapping("/cmuopEntityType")
public class EntityTypeController {

    @Autowired
    private EntityTypeService entityTypeService;

    @GetMapping("/list")
    public WebResponse<PageInfo<CmuopEntityTypeDTO>> queryEntityTypePage(@RequestParam(required = false) String entityName,
                                                                         @RequestParam(required = false) Integer pageNum,
                                                                         @RequestParam(required = false) Integer pageSize) {
        WebResponse<PageInfo<CmuopEntityTypeDTO>> webResponse = new WebResponse<>();
        PageInfo<CmuopEntityTypeDTO> pageInfo = entityTypeService.queryEntityTypePage(entityName, pageNum, pageSize);
        return webResponse.setBody(pageInfo);
    }

    @GetMapping("/detail/{entityCode}")
    public WebResponse<List<CmuopEntityTypeVO>> queryEntityType(@PathVariable String entityCode) {
        WebResponse<List<CmuopEntityTypeVO>> webResponse = new WebResponse<>();
        List<CmuopEntityTypeVO> entityTypeVOList = entityTypeService.queryEntityTypeByCode(entityCode);
        return webResponse.setBody(entityTypeVOList);
    }

    /**
     * 招商观点下新增研报栏目
     * @param entityCode
     * @return
     */
    @GetMapping("/detail/v2/{entityCode}")
    public WebResponse<List<CmuopEntityTypeWithPointInfoTypeVO>> queryEntityTypeV2(@PathVariable String entityCode) {
        WebResponse<List<CmuopEntityTypeWithPointInfoTypeVO>> webResponse = new WebResponse<>();
        List<CmuopEntityTypeWithPointInfoTypeVO> entityTypeVOList = entityTypeService.queryEntityTypeWithPointInfoTypeByCode(entityCode);
        return webResponse.setBody(entityTypeVOList);
    }

    @PostMapping("/update")
    public WebResponse<String> updateEntityType(@RequestBody CmuopEntityTypeDTO cmuopEntityTypeDTO){
        if(cmuopEntityTypeDTO == null
                || cmuopEntityTypeDTO.getEntityId() == null
                || cmuopEntityTypeDTO.getEntityCode() == null) {
            return WebResponse.error("参数错误", cmuopEntityTypeDTO.toString());
        }
        int result = entityTypeService.updateEntityType(cmuopEntityTypeDTO);
        return result > 0 ? WebResponse.success("更新成功") : WebResponse.error("更新失败", null);
    }


    @PostMapping("/delete")
    public WebResponse<String> deleteEntityType(@RequestBody CmuopEntityTypeDTO cmuopEntityTypeDTO){
        if(cmuopEntityTypeDTO == null
                || cmuopEntityTypeDTO.getEntityId() == null
                || cmuopEntityTypeDTO.getEntityCode() == null) {
            return WebResponse.error("参数错误", cmuopEntityTypeDTO.toString());
        }
        int result = entityTypeService.deleteEntityType(cmuopEntityTypeDTO);
        return result > 0 ? WebResponse.success("删除成功") : WebResponse.error("删除失败", null);
    }

}
