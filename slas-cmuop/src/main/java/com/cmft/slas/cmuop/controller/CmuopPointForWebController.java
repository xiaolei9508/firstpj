package com.cmft.slas.cmuop.controller;

import com.cmft.slas.admin.interceptor.TokenAuthorization;
import com.cmft.slas.cmuop.service.CmuopPointService;
import com.cmft.slas.cmuop.vo.PointInfoVO;
import com.cmft.slas.common.pojo.WebResponse;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cmuop/web/pointInfo")
public class CmuopPointForWebController {

    @Autowired
    private CmuopPointService cmuopPointService;

    @TokenAuthorization
    @GetMapping("/list")
    public WebResponse<PageInfo<PointInfoVO>> queryPointInfoPage(@RequestParam String infoType,
                                                               @RequestParam(required = false) String title,
                                                               @RequestParam(required = false) Integer pageNum,
                                                               @RequestParam(required = false) Integer pageSize) {
        // if(!PointInfoConst.isValid(infoType)) {
        // infoType = PointInfoConst.THEMATIC.getValue();
        // }
        WebResponse<PageInfo<PointInfoVO>> webResponse = new WebResponse<>();
        PageInfo<PointInfoVO> pageInfo =
            cmuopPointService.queryPointInfoPage(infoType, title, null, pageNum, pageSize);
        return webResponse.setBody(pageInfo);
    }

    @GetMapping("/{infoId}")
    public WebResponse<PointInfoVO> queryPointInfo(@PathVariable String infoId) {
        return cmuopPointService.queryPointInfoByInfoId(infoId);
    }

    @PostMapping("")
    public WebResponse<String> addPointInfo(@RequestBody PointInfoVO pointInfoVO, @RequestParam Boolean haveOutline) {
        return cmuopPointService.addPointInfo(pointInfoVO, haveOutline);
    }

    @PutMapping("")
    public WebResponse<String> updatePointInfo(@RequestBody PointInfoVO pointInfoVO,
        @RequestParam Boolean haveOutline) {
        return cmuopPointService.updatePointInfo(pointInfoVO, haveOutline);
    }

    @DeleteMapping("/{infoId}")
    public WebResponse<String> deletePointInfoByInfoId(@PathVariable String infoId) {
        return cmuopPointService.deletePointInfoByInfoId(infoId);
    }

    /**
     * 单独修改月报缩略图
     * @param pointInfoVO
     * @return
     */
    @PutMapping("/monthly/imgUrl")
    public WebResponse<String> updateMonthlyImgUrl(@RequestBody PointInfoVO pointInfoVO) {
        return cmuopPointService.updateMonthlyImgUrl(pointInfoVO);
    }
}
