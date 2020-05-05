package com.cmft.slas.cmuop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cmft.slas.cmuop.common.constant.PointInfoConst;
import com.cmft.slas.cmuop.service.CmuopPointViewTypeService;
import com.cmft.slas.cmuop.vo.PointViewTypeVO;
import com.cmft.slas.common.pojo.Page;
import com.cmft.slas.common.pojo.WebResponse;
import com.github.pagehelper.PageInfo;

/**
 * 招商观点栏目设置
 */
@RestController
@RequestMapping("/cmuop/pointViewType")
public class CmuopPointViewTypeController {

    @Autowired
    private CmuopPointViewTypeService cmuopPointViewTypeService;

    @GetMapping("/list")
    public WebResponse<PageInfo<PointViewTypeVO>> getPointViewList(@ModelAttribute Page page) {
        return WebResponse.success(cmuopPointViewTypeService.getPointViewPage(false, page));
    }

    @GetMapping("/{infoType}")
    public WebResponse<PointViewTypeVO> getPointView(@PathVariable String infoType) {
        return WebResponse.success(cmuopPointViewTypeService.getPointView(infoType));
    }

    @PostMapping("")
    public WebResponse<Integer> addPointView(@RequestBody PointViewTypeVO pointViewTypeVO) {
        int res = cmuopPointViewTypeService.addPointView(pointViewTypeVO);
        return res == 1 ? WebResponse.success(res,"新增成功")
                : WebResponse.error("新增失败",res);
    }

    @PutMapping("/{infoType}/updateIfShow")
    public WebResponse<String> updateIfShow(@PathVariable String infoType, @RequestParam Boolean ifShow) {
        String body = cmuopPointViewTypeService.updateIfShow(infoType, ifShow);
        return "操作成功".equals(body) ? WebResponse.success(body, body) : WebResponse.error(body, body);
    }

    @PutMapping("")
    public WebResponse<Integer> updatePointView(@RequestBody PointViewTypeVO pointViewTypeVO) {
        int res = cmuopPointViewTypeService.updatePointView(pointViewTypeVO);
        return res == 1 ? WebResponse.success(res,"更新成功")
                : WebResponse.error("更新失败",res);
    }

    @DeleteMapping("/{infoType}")
    public WebResponse<Integer> deletePointView(@PathVariable String infoType) {
        if (PointInfoConst.OTHER.getValue().equals(infoType))
            return WebResponse.error("其他观点不可删除", 0);
        int res = cmuopPointViewTypeService.deletePointView(infoType);
        return res == 1 ? WebResponse.success(res,"删除成功")
                : WebResponse.error("删除失败",res);
    }

}
