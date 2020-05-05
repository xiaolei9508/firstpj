package com.cmft.slas.cmuop.controller;

import com.cmft.slas.cmuop.dto.UserNotLikeDTO;
import com.cmft.slas.cmuop.service.ArticleBlockingService;
import com.cmft.slas.cmuop.vo.BlockingReasonsVO;
import com.cmft.slas.common.pojo.WebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app")
public class BlockArticleController {

    @Autowired
    private ArticleBlockingService articleBlockingService;

    /**
     * reason list should be formalizing into one string in the format of a json list.
     *
     * @param userNotLikeDTO
     * @return
     */
    @PostMapping("/block")
    public WebResponse<String> addUserBlock(@RequestBody UserNotLikeDTO userNotLikeDTO) {
        return WebResponse.success(articleBlockingService.BlockArticle(userNotLikeDTO));
    }

    /**
     * Displayed reasons should contains this list and a particular reason regarding to user's dislike towards article's source or author,
     * which is not returned for this method.
     * When the reasons are recorded, dislike of source or article should differentiate from other reasons.
     * @return
     */
    @GetMapping("/reasons")
    public WebResponse<List<BlockingReasonsVO>> getReasons(){
        return WebResponse.success(articleBlockingService.getBlockingReasons());
    }
}
