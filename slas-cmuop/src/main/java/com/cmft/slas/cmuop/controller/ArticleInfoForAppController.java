package com.cmft.slas.cmuop.controller;

import com.cmft.slas.cmuop.service.ArticleForAppService;
import com.cmft.slas.cmuop.vo.ArticleVO;
import com.cmft.slas.common.pojo.WebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文章详情接口（APP）
 *
 * @author wuk001
 * @date 2020/12/30
 */
@RestController
@RequestMapping("/app/article")
public class ArticleInfoForAppController {

    @Autowired
    private ArticleForAppService articleForAppService;

    @GetMapping("/{articleId}")
    public WebResponse<ArticleVO> getArticleInfo(@PathVariable("articleId") String articleId,
                                                 @RequestParam String uid) {
        return new WebResponse<>(articleForAppService.getArticleDetail(articleId, uid));
    }

}
