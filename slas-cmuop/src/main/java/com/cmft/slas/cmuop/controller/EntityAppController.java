package com.cmft.slas.cmuop.controller;

import java.util.List;

import com.cmft.slas.cmuop.service.EntityAppService;
import com.cmft.slas.cmuop.vo.EntityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cmft.slas.cmuop.dto.UserEntityDTO;
import com.cmft.slas.cmuop.vo.EntityTrendVO;
import com.cmft.slas.common.pojo.WebResponse;

/**
 * 实体controller
 * 
 * @author wuk001
 * @date 2020/12/30
 */
@RestController
@RequestMapping("/app")
public class EntityAppController {

    @Autowired
    private EntityAppService entityAppService;

    @GetMapping("/entities")
    public WebResponse<List<EntityVO>> getEntitys(@RequestParam(required = false) String uid) {
        return new WebResponse<>(entityAppService.getAllEntities(uid));
    }

    @GetMapping("/entity/trend")
    public WebResponse<List<EntityTrendVO>> getEntityTrend(@RequestParam String uid,
                                                           @RequestParam(required = false) Integer dateBackNum) {
        return new WebResponse<>(entityAppService.getEntityStatWRTUser(uid, dateBackNum == null ? 30 : dateBackNum));
    }

    @GetMapping("/entity/{entityCode}")
    public WebResponse<EntityTrendVO> getEntityStat(@PathVariable String entityCode,
                                                    @RequestParam(required = false) Integer dateBackNum){
        return new WebResponse<>(entityAppService.getEntityStat(entityCode, dateBackNum == null ? 30 : dateBackNum));
    }

    @GetMapping("/entity/reload")
    public WebResponse<String> reloadEntity() {
        return WebResponse.success(entityAppService.reloadEntityList());
    }
}
