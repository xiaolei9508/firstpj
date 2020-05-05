package com.cmft.slas.cmuop.controller;

import java.util.List;

import com.cmft.slas.cmuop.entity.PointInfoViewRecord;
import com.cmft.slas.cmuop.service.CmuopPointService;
import com.cmft.slas.cmuop.vo.PointInfoVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import com.cmft.slas.cmuop.service.ArticleListForAppService;
import com.cmft.slas.cmuop.service.ArticleListSortService;
import com.cmft.slas.cmuop.vo.AppArticlePreview;
import com.cmft.slas.common.pojo.PageInfo;
import com.cmft.slas.common.pojo.WebResponse;

@RestController
@RequestMapping("/app")
public class ArticleListController {

    @Autowired
    private ArticleListForAppService articleListForAppService;

    @Autowired
    private ArticleListSortService articleListSortAppService;

    @Autowired
    private CmuopPointService cmuopPointService;

    @GetMapping("/carousels")
    public WebResponse<List<AppArticlePreview>> getCarouselPages(
        @RequestParam String uid,
        @RequestParam int pageSize,
        @RequestParam(required = false, name = "lastPosition") String lastPosition) {
        return WebResponse.success(articleListForAppService.getArticleListForCarousels(uid));
    }

    @GetMapping("/articles")
    public WebResponse<PageInfo<AppArticlePreview>> getArticlePages(@RequestParam String uid,
        @RequestParam int pageSize, @RequestParam(required = false, name = "entityType") String entityType,
        @RequestParam(required = false, name = "sentimentType") String sentimentType,
        @RequestParam(required = false, name = "columnType") String columnType,
        @RequestParam(required = false, name = "lastPosition") String lastPosition,
        @RequestParam(required = false, name = "ifFirstTime") Boolean ifFirstTime) {
        PageInfo<AppArticlePreview> res = articleListSortAppService.getSortedArticleList(uid,
                entityType,
                sentimentType,
                columnType,
                lastPosition,
                pageSize,
                ifFirstTime == null ? false : ifFirstTime);
        return WebResponse.success(res);
    }

    /**
     * 研报列表展示
     * @param infoType
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/pointInfo/list")
    public WebResponse<com.github.pagehelper.PageInfo<PointInfoVO>> queryPointInfoPage(@RequestParam(required = false) String uid,
        @RequestParam String infoType,
                                                                                       @RequestParam(required = false) Integer pageNum,
                                                                                       @RequestParam(required = false) Integer pageSize) {
        WebResponse<com.github.pagehelper.PageInfo<PointInfoVO>> webResponse = new WebResponse<>();
        com.github.pagehelper.PageInfo<PointInfoVO> pageInfo =
            cmuopPointService.queryPointInfoPage(infoType, null, true, pageNum, pageSize);

        if(StringUtils.isNotBlank(uid) && !CollectionUtils.isEmpty(pageInfo.getList())) {
            for(PointInfoVO vo : pageInfo.getList()) {
                vo.setIfRead(cmuopPointService.ifRead(uid, vo.getInfoId()));
            }
        }
        return webResponse.setBody(pageInfo);
    }

    /**
     * 研报浏览记录
     * @param viewRecord
     * @return
     */
    @PostMapping("/pointInfo/viewRecord")
    public WebResponse<String> addViewRecordForPointInfo(@RequestBody PointInfoViewRecord viewRecord) {
        WebResponse<String> res = new WebResponse<>();
        try {
            res = cmuopPointService.addPointInfoViewRecord(viewRecord);
        } catch (Exception e) {
            return WebResponse.error("新增浏览记录失败", null);
        }
        return res;
    }

}
