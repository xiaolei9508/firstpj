package com.cmft.slas.cmuop.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.cmft.slas.admin.aspect.SystemLogger;
import com.cmft.slas.admin.interceptor.TokenAuthorization;
import org.apache.commons.collections.CollectionUtils;
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
import com.cmft.slas.cmuop.dto.AllArticleDTO;
import com.cmft.slas.cmuop.dto.ArticleRelatedEntityDTO;
import com.cmft.slas.cmuop.dto.ChangeOrderDTO;
import com.cmft.slas.cmuop.dto.StatDTO;
import com.cmft.slas.cmuop.entity.Article;
import com.cmft.slas.cmuop.entity.ArticleRelatedEntity;
import com.cmft.slas.cmuop.mapper.ArticleRelatedEntityMapper;
import com.cmft.slas.cmuop.service.AllArticleWebService;
import com.cmft.slas.cmuop.service.ArticleBackUpService;
import com.cmft.slas.cmuop.service.ArticleForAppService;
import com.cmft.slas.cmuop.service.ArticleRelatedEntityService;
import com.cmft.slas.cmuop.service.ArticleReloadService;
import com.cmft.slas.cmuop.vo.AllArticleDetail;
import com.cmft.slas.cmuop.vo.AllArticlePreview;
import com.cmft.slas.cmuop.vo.EntityStat;
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
@RequestMapping("/headline/cmuop/admin/allInfos")
@Api(value = "all_info_api", description = "cmuop headline")
public class AllArticleWebController {

    @Autowired
    private AllArticleWebService allArticleWebService;
    
    @Autowired
    private ArticleBackUpService articleBackUpService;
    
    @Autowired
    private ArticleReloadService articleReloadService;

    @Autowired
    private ArticleRelatedEntityMapper articleRelatedEntityMapper;

    @Autowired
    private ArticleForAppService articleForAppService;

    @Autowired
    private ArticleRelatedEntityService articleRelatedEntityService;

    /**
     * in statDTO, choices for entity and thirdLevel can be at most one
     * @param page
     * @param timeRange
     * @param title
     * @param statDTO
     * @return
     */
    @TokenAuthorization
    @ApiOperation(value = "get all article list in page", consumes = "application/json", produces = "application/json")
    @GetMapping("/articles")
    public WebResponse<PageInfo<AllArticlePreview>> articleList(@ModelAttribute Page page,
        @ModelAttribute TimeRangeDTO timeRange, @RequestParam(required = false) String title,
        @ModelAttribute StatDTO statDTO, @RequestParam(required = false) Boolean ifStick) {
        val pageInfo = allArticleWebService.getArticleList(page,
            timeRange.getTimeRange() == null ? null : timeRange.getTimeRange().getFrom(),
            timeRange.getTimeRange() == null ? null : timeRange.getTimeRange().getTo(), title, statDTO, ifStick);
        return WebResponse.success(pageInfo);
    }

    @TokenAuthorization
    @ApiOperation(value = "get an article detail", consumes = "application/json", produces = "application/json")
    @GetMapping("/articles/{articleId}")
    public WebResponse<AllArticleDetail> articleDetail(@PathVariable String articleId) {
        Article article = allArticleWebService.getArticleByArticleId(articleId);
        if (article == null) {
            return WebResponse.<AllArticleDetail>error("找不到该文章", null);
        }
        AllArticleDetail allArticleDetail = BeanMapper.map(article, AllArticleDetail.class);
        allArticleWebService.makeUpAllArticleDetail(articleId, allArticleDetail);
        return WebResponse.<AllArticleDetail>success(allArticleDetail);
    }

    @TokenAuthorization
    @SystemLogger(type = "修改文章详情", articleId = "#articleId")
    @ApiOperation(value = "modify an article's detail through detail page", consumes = "application/json", produces = "application/json")
    @PutMapping("/articles/{articleId}")
    public WebResponse<String> detailModification(@PathVariable String articleId,
                                                  @RequestBody AllArticleDTO allArticleDTO) throws IllegalAccessException, InvocationTargetException {
        return WebResponse.<String>success(allArticleWebService.updateArticle(articleId, allArticleDTO));
    }

    @TokenAuthorization
    @PutMapping("/articles/{articleId}/orderNum")
    @ApiOperation("carousel orderNum update")
    public WebResponse<String> orderNumModification(@PathVariable String articleId,
                                                    @RequestParam Integer orderNum) throws InvocationTargetException, IllegalAccessException {
        AllArticleDTO dto = new AllArticleDTO();
        dto.setArticleId(articleId);
        dto.setOrderNum(orderNum);
        return detailModification(articleId, dto);
    }

    @TokenAuthorization
    @SystemLogger(type = "修改文章情感标签", articleId = "#articleId")
    @ApiOperation(value = "modify an article's sentiment in preview list", consumes = "application/json", produces = "application/json")
    @PutMapping("/articles/{articleId}/sentiment")
    public WebResponse<String> sentimentModification(@PathVariable String articleId,
                                                     @RequestParam Integer sentiment) {
        Article article = allArticleWebService.getArticleByArticleId(articleId);
        if (article == null) {
            return WebResponse.<String>error("没找到该文章", "没找到该文章");
        }
        article.setSentiment(sentiment);
        article.setUpdateTime(new Date());
        allArticleWebService.updateArticleByPK(article);
        articleReloadService.reload(article);
        if (articleForAppService.checkIfSaveInRedis(article)) {
            articleBackUpService.backUpArticleToRedis(article);
        }
        return WebResponse.<String>success("更新成功");
    }

    @TokenAuthorization
    @SystemLogger(type = "修改文章是否显示", articleId = "#articleId")
    @ApiOperation(value = "modify an article's ifShow in preview list", consumes = "application/json", produces = "application/json")
    @PutMapping("/articles/{articleId}/changeIfShow")
    public WebResponse<String> ifShowModification(@PathVariable String articleId,
                                                  @RequestParam Boolean ifShow) {
        Article article = allArticleWebService.getArticleByArticleId(articleId);
        if (article == null) {
            return WebResponse.<String>error("没找到该文章", "没找到该文章");
        }
        article.setIfShow(ifShow);
        if(!ifShow) {
            article.setIfRecommend(false);
            article.setUpdateTime(new Date());
            articleRelatedEntityService.batchProcessEntityWithRecommend(articleId, false);
            article.setIfPartStick(false);
            allArticleWebService.resetOrderNum(articleId, false);
            article.setIfAllStick(false);
        }
        allArticleWebService.updateArticleByPK(article);
        articleReloadService.reload(article);
        if (articleForAppService.checkIfSaveInRedis(article)) {
            articleBackUpService.backUpArticleToRedis(article);
        }
        return WebResponse.<String>success("更新成功");
    }

    @TokenAuthorization
    @ApiOperation(value = "modify an article's ifStick value from preview list")
    @PutMapping("/articles/{articleId}/ifStick")
    public WebResponse<String> ifStickModification(@PathVariable String articleId, @RequestParam Boolean ifStick)
        throws InvocationTargetException, IllegalAccessException {
        AllArticleDTO allArticleDTO = new AllArticleDTO();
        allArticleDTO.setIfStick(ifStick);
        if(ifStick) {
            allArticleDTO.setIfRecommend(false);
            allArticleDTO.setIfAllStick(false);
            allArticleDTO.setIfPartStick(false);
        }
        String body = allArticleWebService.updateArticle(articleId, allArticleDTO);
        articleRelatedEntityService.processAllEntityWithRecommend(articleId, false);
        return WebResponse.<String>success(body);
    }

    @TokenAuthorization
    @ApiOperation(value = "modify an article's ifRecommend in preview list", consumes = "application/json",
        produces = "application/json")
    @PutMapping("/articles/{articleId}/changeIfRecommend")
    public WebResponse<String> ifRecommendModification(@PathVariable String articleId,
        @RequestParam Boolean ifRecommend) {
        Article article = allArticleWebService.getArticleByArticleId(articleId);
        if (article == null) {
            return WebResponse.<String>error("没找到该文章", "没找到该文章");
        }
        if(ifRecommend && !articleRelatedEntityService.checkIfEntityExists(articleId)){
            return WebResponse.error("推荐失败，非招闻天下、非行业新闻的舆情主体不能为空","推荐失败，非招闻天下、非行业新闻的舆情主体不能为空");
        }
        article.setIfRecommend(ifRecommend);
        article.setUpdateTime(new Date());
        if (ifRecommend) {
            article.setIfShow(true);
            articleRelatedEntityService.batchProcessEntityWithRecommend(articleId, true);

        } else {
            articleRelatedEntityService.batchProcessEntityWithRecommend(articleId, false);
            article.setIfAllStick(false);
        }

        allArticleWebService.updateArticleByPK(article);
        articleReloadService.reload(article);
        if (articleForAppService.checkIfSaveInRedis(article)) {
            articleBackUpService.backUpArticleToRedis(article);
        }
        return WebResponse.<String>success("更新成功");
    }

    @TokenAuthorization
    @ApiOperation(value = "modify an article's allSticky status in preview list", consumes = "application/json", produces = "application/json")
    @PutMapping("/articles/{articleId}/updateSticky/all")
    public WebResponse<String> stickyModification4All(@PathVariable String articleId,
                                                      @RequestParam Boolean sticky) {
        Article article = allArticleWebService.getArticleByArticleId(articleId);
        if (article == null) {
            return WebResponse.<String>error("没找到该文章", "没找到该文章");
        }
        if(sticky && !articleRelatedEntityService.checkIfEntityExists(articleId)){
            return WebResponse.error("置顶失败，非招闻天下、非行业新闻的舆情主体不能为空","置顶失败，非招闻天下、非行业新闻的舆情主体不能为空");
        }
        article.setIfAllStick(sticky);
        article.setUpdateTime(new Date());
        if (sticky) {
            article.setIfShow(true);
            article.setIfRecommend(true);
            articleRelatedEntityService.batchProcessEntityWithRecommend(articleId, true);
        } else {
            allArticleWebService.resetOrderNum(articleId, true);
        }
        allArticleWebService.updateArticleByPK(article);
        articleReloadService.reload(article);
        if (articleForAppService.checkIfSaveInRedis(article)) {
            articleBackUpService.backUpArticleToRedis(article);
        }
        return WebResponse.<String>success("更新成功");
    }

    @TokenAuthorization
    @ApiOperation(value = "modify an article's moduleSticky status in preview list", consumes = "application/json", produces = "application/json")
    @PutMapping("/articles/{articleId}/updateSticky/module")
    public WebResponse<String> stickyModification4Module(@PathVariable String articleId,
                                                         @RequestParam Boolean sticky) {
        Article article = allArticleWebService.getArticleByArticleId(articleId);
        if (article == null) {
            return WebResponse.<String>error("没找到该文章", "没找到该文章");
        }
        article.setIfPartStick(sticky);
        article.setUpdateTime(new Date());
        if (sticky) {
            article.setIfShow(true);
        } else {
            allArticleWebService.resetOrderNum(articleId, false);
        }
        allArticleWebService.updateArticleByPK(article);
        articleReloadService.reload(article);
        if (articleForAppService.checkIfSaveInRedis(article)) {
            articleBackUpService.backUpArticleToRedis(article);
        }
        return WebResponse.<String>success("更新成功");
    }

    @ApiOperation(value = "article counting result w.r.t grouping condition", consumes = "application/json", produces = "application/json")
    @GetMapping("/stat")
    public WebResponse<EntityStat> entityStat(@RequestParam(required = false) String title,
        @ModelAttribute TimeRangeDTO timeRange,
        @ModelAttribute StatDTO statDTO) {
        return WebResponse.<EntityStat>success(allArticleWebService.getEntityStat(
            timeRange.getTimeRange() == null ? null : timeRange.getTimeRange().getFrom(),
            timeRange.getTimeRange() == null ? null : timeRange.getTimeRange().getTo(), title, statDTO));
    }

    @ApiOperation(value = "exporting all articles under current condition")
    @GetMapping("/export")
    public void exportArticles(@ModelAttribute TimeRangeDTO timeRange, @RequestParam(required = false) String title,
        @ModelAttribute StatDTO statDTO, @RequestParam(required = false)Boolean ifStick, HttpServletResponse response) {
        try {
            OutputStream os = response.getOutputStream();
            InputStream inputStream = allArticleWebService.exportExcel(
                timeRange.getTimeRange() == null ? null : timeRange.getTimeRange().getFrom(),
                timeRange.getTimeRange() == null ? null : timeRange.getTimeRange().getTo(), title, statDTO, ifStick);
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

    @TokenAuthorization
    @ApiOperation(value = "modify an article's orderNum in preview list", consumes = "application/json",
        produces = "application/json")
    @PutMapping("/articles/{articleId}/updateOrderNum")
    public WebResponse<String> orderNumModification4Module(@PathVariable String articleId,
        @RequestBody ChangeOrderDTO changeOrderDTO) {
        ArticleRelatedEntityDTO params = new ArticleRelatedEntityDTO();
        params.setArticleId(articleId);
        params.setEntityCode(changeOrderDTO.getEntityCode());
        params.setColumnType(changeOrderDTO.getType());
        List<ArticleRelatedEntity> list = articleRelatedEntityMapper.queryByCondition(params);
        if (CollectionUtils.isEmpty(list)) {
            return WebResponse.<String>error("没找到该文章", "没找到该文章");
        }
        ArticleRelatedEntity articleRelatedEntity = list.get(0);
        articleRelatedEntity.setOrderNum(changeOrderDTO.getOrderNum());
        articleRelatedEntityMapper.updateByPrimaryKeySelective(articleRelatedEntity);
        return WebResponse.<String>success("更新成功");
    }

    @ApiOperation(value = "modify an article's ifShow in preview list", consumes = "application/json",
        produces = "application/json")
    @PutMapping("/articles/{articleId}/fixIfShow")
    public WebResponse<String> ifShowfix(@PathVariable String articleId, @RequestParam Boolean ifShow) {
        log.info("nlp fix article: {} if show", articleId);
        Article article = allArticleWebService.getArticleByArticleId(articleId);
        if (article == null) {
            return WebResponse.<String>error("没找到该文章", "没找到该文章");
        }
        article.setIfShow(ifShow);
        article.setUpdateTime(new Date());
        allArticleWebService.updateArticleByPK(article);
        articleReloadService.reload(article);
        if (articleForAppService.checkIfSaveInRedis(article)) {
            articleBackUpService.backUpArticleToRedis(article);
        }
        return WebResponse.<String>success("更新成功");
    }
}
