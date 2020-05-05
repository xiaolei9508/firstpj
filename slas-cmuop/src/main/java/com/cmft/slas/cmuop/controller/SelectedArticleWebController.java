package com.cmft.slas.cmuop.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cmft.slas.cmuop.common.dto.TimeRangeDTO;
import com.cmft.slas.cmuop.dto.SelectedArticleDTO;
import com.cmft.slas.cmuop.entity.Article;
import com.cmft.slas.cmuop.service.AllArticleWebService;
import com.cmft.slas.cmuop.service.ArticleBackUpService;
import com.cmft.slas.cmuop.service.ArticleForAppService;
import com.cmft.slas.cmuop.service.ArticleReloadService;
import com.cmft.slas.cmuop.service.SelectedArticleWebService;
import com.cmft.slas.cmuop.vo.SelectedDetail;
import com.cmft.slas.cmuop.vo.SelectedPreview;
import com.cmft.slas.common.pojo.Page;
import com.cmft.slas.common.pojo.WebResponse;
import com.cmft.slas.common.utils.BeanMapper;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author liurp001
 * @Since 2019/12/27
 */
@Slf4j
@RestController
@RequestMapping("/headline/cmuop/admin/selectedInfos")
@Api(value = "selected_articles", description = "select article interface")
public class SelectedArticleWebController {

    @Autowired
    private SelectedArticleWebService selectedArticleWebService;

    @Autowired
    private AllArticleWebService allArticleWebService;

    @Autowired
    private ArticleBackUpService articleBackUpService;

    @Autowired
    private ArticleReloadService articleReloadService;

    @Autowired
    private ArticleForAppService articleForAppService;

    @ApiOperation(value = "get selected article's preview page", consumes = "application/json", produces = "application/json")
    @GetMapping("/articles")
    public WebResponse<PageInfo<SelectedPreview>> articleList(@ModelAttribute Page page,
                                                              @ModelAttribute TimeRangeDTO timeRange,
                                                              @RequestParam(required = false) String title) {
        val info = selectedArticleWebService.getArticleList(page,
            timeRange.getTimeRange() == null ? null : timeRange.getTimeRange().getFrom(),
            timeRange.getTimeRange() == null ? null : timeRange.getTimeRange().getTo(), title);
        return WebResponse.success(info);
    }

    @ApiOperation(value = "get a selected article's detail", consumes = "application/json", produces = "application/json")
    @GetMapping("/articles/{articleId}")
    public WebResponse<SelectedDetail> articleDetail(@PathVariable String articleId) {
        Article article = allArticleWebService.getArticleByArticleId(articleId);
        SelectedDetail selectedDetail = BeanMapper.map(article, SelectedDetail.class);
        allArticleWebService.makeUpAllArticleDetail(articleId, selectedDetail);
        return WebResponse.<SelectedDetail>success(selectedDetail);
    }

    @ApiOperation(value = "modify an article's detail through detail page", consumes = "application/json", produces = "application/json")
    @PutMapping("/articles/{articleId}")
    public WebResponse<String> detailModification(@PathVariable String articleId,
        @RequestBody SelectedArticleDTO selectedArticleDTO) throws IllegalAccessException, InvocationTargetException {
        return WebResponse.<String>success(selectedArticleWebService.updateArticle(articleId, selectedArticleDTO));
    }

    @ApiOperation(value = "modify an article's sentiment in preview list", consumes = "application/json", produces = "application/json")
    @PutMapping("/articles/{articleId}/sentiment")
    public WebResponse<String> sentimentModification(@PathVariable String articleId,
                                                     @RequestParam Integer sentiment) {
        Article article = allArticleWebService.getArticleByArticleId(articleId);
        if (article == null) {
            return WebResponse.<String>error("没找到该文章", "没找到该文章");
        }
        article.setSentiment(sentiment);
        allArticleWebService.updateArticleByPK(article);
        articleReloadService.reload(article);
        if (articleForAppService.checkIfSaveInRedis(article)) {
            articleBackUpService.backUpArticleToRedis(article);
        }
        return WebResponse.<String>success("更新成功");
    }

    @ApiOperation(value = "modify an article's ifShow in preview list", consumes = "application/json", produces = "application/json")
    @PutMapping("/articles/{articleId}/changeIfShow")
    public WebResponse<String> ifShowModification(@PathVariable String articleId,
                                                  @RequestParam Boolean ifShow) {
        Article article = allArticleWebService.getArticleByArticleId(articleId);
        if (article == null) {
            return WebResponse.<String>error("没找到该文章", "没找到该文章");
        }
        article.setIfShow(ifShow);
        allArticleWebService.updateArticleByPK(article);
        articleReloadService.reload(article);
        if (articleForAppService.checkIfSaveInRedis(article)) {
            articleBackUpService.backUpArticleToRedis(article);
        }
        return WebResponse.<String>success("更新成功");
    }

    @ApiOperation(value = "modify an article's sticky status in preview list", consumes = "application/json",
        produces = "application/json")
    @PutMapping("/articles/{articleId}/updateSticky/all")
    public WebResponse<String> stickyModification4All(@PathVariable String articleId,
                                                      @RequestParam Boolean sticky) {
        Article article = allArticleWebService.getArticleByArticleId(articleId);
        if (article == null) {
            return WebResponse.<String>error("没找到该文章", "没找到该文章");
        }
        article.setIfStick(sticky);
        article.setStickUpdateTime(new Date());
        if (sticky) {
            article.setIfShow(true);
        }
        allArticleWebService.updateArticleByPK(article);
        articleReloadService.reload(article);
        if (articleForAppService.checkIfSaveInRedis(article)) {
            articleBackUpService.backUpArticleToRedis(article);
        }
        return WebResponse.<String>success("更新成功");
    }


    @ApiOperation(value = "exporting all articles under current condition")
    @GetMapping("/export")
    public void exportArticles(@ModelAttribute TimeRangeDTO timeRange,
        @RequestParam(required = false) String title, HttpServletResponse response) {
        try {
            OutputStream os = response.getOutputStream();
            InputStream inputStream = selectedArticleWebService.exportExcel(
                timeRange.getTimeRange() == null ? null : timeRange.getTimeRange().getFrom(),
                timeRange.getTimeRange() == null ? null : timeRange.getTimeRange().getTo(), title);
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }

            // 这里主要关闭。
            os.close();
            inputStream.close();
        } catch (Exception e) {
            log.error("get excel error: " + ExceptionUtils.getStackTrace(e));
        }
    }
}
