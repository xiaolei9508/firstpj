package com.cmft.slas.cmuop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cmft.slas.cmuop.service.ArticleReloadService;
import com.cmft.slas.common.pojo.WebResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/reloadArticleToRedis")
@Api(value = "reload redis")
public class ArticleReloadController {

    @Autowired
    private ArticleReloadService articleReloadService;

    @ApiOperation(value = "reolad article to redis entity set", consumes = "application/json",
        produces = "application/json")
    @GetMapping("/reload")
    public WebResponse<String> reload() {
        articleReloadService.reload();
        return WebResponse.<String>success("success");
    }

    @ApiOperation(value = "if update thumbnail", consumes = "application/json", produces = "application/json")
    @GetMapping("/ifUpdateThumbnail/{ifUpdate}")
    public WebResponse<String> ifUpdateThumbnail(@PathVariable Boolean ifUpdate) {
        articleReloadService.ifUpdateThumbnail(ifUpdate);
        return WebResponse.<String>success("success");
    }
}
