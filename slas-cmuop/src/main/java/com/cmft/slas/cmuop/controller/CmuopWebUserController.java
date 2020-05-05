package com.cmft.slas.cmuop.controller;

import com.cmft.slas.cmuop.service.CmuopWebUserService;
import com.cmft.slas.cmuop.vo.LeaderPreview;

import com.cmft.slas.common.pojo.WebResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cmuop/user")
public class CmuopWebUserController {
    @Autowired
    private CmuopWebUserService webUserService;

    @GetMapping("/leaders")
    public WebResponse<List<LeaderPreview>> getLeadersList(@RequestParam String entityCode){
        return WebResponse.success(webUserService.getLeaderList(entityCode));
    }

    @PutMapping("/leaders/{uid}")
    public WebResponse<String> updateLeaderOrder(@PathVariable String uid,
                                                 @RequestParam String entityCode,
                                                 @RequestParam Integer orderNum){
        return WebResponse.success(webUserService.updateLeaderOrder(uid, entityCode, orderNum));
    }

    @PutMapping("/leaders")
    public WebResponse updateEntity(@RequestParam String entityCode){
        return WebResponse.success(webUserService.updateEntity(entityCode));
    }

}
