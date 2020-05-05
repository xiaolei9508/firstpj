 package com.cmft.slas.cmuop.processor.processorImpl;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cmft.slas.cmuop.dto.ArticleForSortDTO;
import com.cmft.slas.cmuop.entity.Article;
import com.cmft.slas.cmuop.entity.ArticleAuthorizedEntities;
import com.cmft.slas.cmuop.entity.OaAuthority;
import com.cmft.slas.cmuop.entity.UserNotLike;
import com.cmft.slas.cmuop.mapper.ArticleAuthorizedEntitiesMapper;
import com.cmft.slas.cmuop.mapper.ArticleMapper;
import com.cmft.slas.cmuop.mapper.OaAuthorityMapper;
import com.cmft.slas.cmuop.mapper.UserNotLikeMapper;
import com.cmft.slas.cmuop.processor.ArticleFilterProcessor;
import com.cmft.slas.cmuop.vo.AppArticlePreview;
import com.google.common.collect.Lists;

import tk.mybatis.mapper.entity.Example;

@Component
public class ArticleFilterProcessorImpl implements ArticleFilterProcessor {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleAuthorizedEntitiesMapper articleAuthorizedEntitiesMapper;

    @Autowired
    private OaAuthorityMapper oaAuthorityMapper;

    @Autowired
    private UserNotLikeMapper userNotLikeMapper;

    @Override
    public List<AppArticlePreview> process(List<? extends AppArticlePreview> articleList, String uid, Boolean type) {
        /*
         * 1. check oa authority
         * 2. block
         */
        List<AppArticlePreview> res = Lists.newArrayList();
        // res = filterOaAuthority(articleList, uid);
        res = filterBlockedArticle(articleList, uid, type);
        return res;
    }

    private List<AppArticlePreview> filterOaAuthority(List<? extends AppArticlePreview> articleList, String uid) {

        Example oaexample = new Example(OaAuthority.class);
        oaexample.createCriteria().andEqualTo("uid", uid);
        List<OaAuthority> oaList = oaAuthorityMapper.selectByExample(oaexample);

        List<AppArticlePreview> res = articleList.stream().filter(new Predicate<AppArticlePreview>() {
            @Override
            public boolean test(AppArticlePreview t) {
                if (t.getIfFromOa()) {
                    Example aaexample = new Example(ArticleAuthorizedEntities.class);
                    aaexample.createCriteria().andEqualTo("articleId", t.getArticleId());
                    Map<String, List<String>> aaMap =
                        articleAuthorizedEntitiesMapper.selectByExample(aaexample)
                            .stream()
                            .collect(
                                Collectors.groupingBy(ArticleAuthorizedEntities::getAuthAreaId,
                                Collectors.mapping(ArticleAuthorizedEntities::getEntityCode, Collectors.toList())));
                    Map<String, List<String>> oaMap =
                        oaList.stream().collect(Collectors.groupingBy(OaAuthority::getAuthAreaId,
                            Collectors.mapping(OaAuthority::getAuthReader, Collectors.toList())));

                    Article article = new Article();
                    article.setArticleId(t.getArticleId());
                    article = articleMapper.selectOne(article);
                    List<String> testList =
                        oaList.stream().map(OaAuthority::getAuthAreaId).collect(Collectors.toList());
                    return testList.contains(article.getAuthorizedArea()) && ifCrossed(aaMap, oaMap);
                }
                return true;
            }

        }).collect(Collectors.toList());

        return res;

    }

    private List<AppArticlePreview> filterBlockedArticle(List<? extends AppArticlePreview> articleList, String uid,
        Boolean type) {
        Example userNotLikeExample = new Example(UserNotLike.class);
        userNotLikeExample.createCriteria().andEqualTo("uid", uid);
        List<String> unlList = userNotLikeMapper.selectByExample(userNotLikeExample).stream()
            .map(UserNotLike::getArticleId).collect(Collectors.toList());

        if (type) {
            return articleList.stream().filter(new Predicate<AppArticlePreview>() {

                @Override
                public boolean test(AppArticlePreview t) {
                    if (t.getIfAllStick().equals((byte)0)) {
                        return !unlList.contains(t.getArticleId());
                    }
                    return true;
                }
            }).collect(Collectors.toList());
        } else {
            return articleList.stream().filter(new Predicate<AppArticlePreview>() {

                @Override
                public boolean test(AppArticlePreview t) {
                    if (t.getIfPartStick().equals((byte)0)) {
                        return !unlList.contains(t.getArticleId());
                    }
                    return true;
                }
            }).collect(Collectors.toList());

        }
    }

    private Boolean ifCrossed(Map<String, List<String>> aaMap, Map<String, List<String>> oaMap) {
        for (String key : aaMap.keySet()) {
            List<String> aaList = aaMap.get(key);
            List<String> oaList = oaMap.get(key);

            if (CollectionUtils.isEmpty(aaList) || CollectionUtils.isEmpty(oaList)) {
                continue;
            }

            for (String str : aaList) {
                if (oaList.contains(str)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public List<ArticleForSortDTO> process(String uid, List<ArticleForSortDTO> articleList) {
        /*
         * 1. check oa authority
         * 2. block
         */
        List<ArticleForSortDTO> res = Lists.newArrayList();
        // res = filterOaAuthority(uid, articleList);
        res = filterBlockedArticle(uid, articleList);
        return res;
    }

    private List<ArticleForSortDTO> filterOaAuthority(String uid, List<ArticleForSortDTO> articleList) {

        Example oaexample = new Example(OaAuthority.class);
        oaexample.createCriteria().andEqualTo("uid", uid);
        List<OaAuthority> oaList = oaAuthorityMapper.selectByExample(oaexample);
        List<ArticleForSortDTO> res = Lists.newArrayList();

        List<String> idList = articleList.stream().map(ArticleForSortDTO::getArticleId).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(idList)) {
            return res;
        }
        Example example = new Example(ArticleAuthorizedEntities.class);
        example.createCriteria().andIn("articleId", idList).andEqualTo("isDelete", (byte)0);
        Map<String, List<ArticleAuthorizedEntities>> aaeMap = articleAuthorizedEntitiesMapper.selectByExample(example)
            .stream().collect(Collectors.groupingBy(ArticleAuthorizedEntities::getArticleId));
        example = new Example(Article.class);
        example.createCriteria().andIn("articleId", idList).andEqualTo("isDelete", (byte)0);
        Map<String, String> areaMap = articleMapper.selectByExample(example).stream()
            .collect(Collectors.toMap(Article::getArticleId, Article::getAuthorizedArea));

        Map<String, List<String>> oaMap = oaList.stream().collect(Collectors.groupingBy(OaAuthority::getAuthAreaId,
            Collectors.mapping(OaAuthority::getAuthReader, Collectors.toList())));
        List<String> testList = oaList.stream().map(OaAuthority::getAuthAreaId).collect(Collectors.toList());
        for (ArticleForSortDTO article : articleList) {
            if (article.getIfFromOa()) {
                List<ArticleAuthorizedEntities> aaeList = aaeMap.get(article.getArticleId());
                String area = areaMap.get(article.getArticleId());
                if (CollectionUtils.isEmpty(aaeList) || StringUtils.isBlank(area)) {
                    continue;
                }
                Map<String, List<String>> aaMap =
                    aaeList.stream().collect(Collectors.groupingBy(ArticleAuthorizedEntities::getAuthAreaId,
                        Collectors.mapping(ArticleAuthorizedEntities::getEntityCode, Collectors.toList())));
                if (testList.contains(area) && ifCrossed(aaMap, oaMap)) {
                    res.add(article);
                }
            } else {
                res.add(article);
            }
        }

        return res;

    }

    private List<ArticleForSortDTO> filterBlockedArticle(String uid, List<ArticleForSortDTO> articleList) {
        Example userNotLikeExample = new Example(UserNotLike.class);
        userNotLikeExample.createCriteria().andEqualTo("uid", uid);
        List<String> unlList = userNotLikeMapper.selectByExample(userNotLikeExample).stream()
            .map(UserNotLike::getArticleId).collect(Collectors.toList());

        List<ArticleForSortDTO> temp = articleList.stream().filter(new Predicate<ArticleForSortDTO>() {

            @Override
            public boolean test(ArticleForSortDTO t) {
                if (t.getIfAllStick().equals((byte)0) || t.getIfPartStick().equals((byte)0)) {
                    return !unlList.contains(t.getArticleId());
                }
                return true;
            }
        }).collect(Collectors.toList());
        return temp;
    }

    @Override
    public List<ArticleForSortDTO> process(String uid, List<ArticleForSortDTO> articleList, Boolean type) {
        // only filter not like
        Example userNotLikeExample = new Example(UserNotLike.class);
        userNotLikeExample.createCriteria().andEqualTo("uid", uid);
        List<String> unlList = userNotLikeMapper.selectByExample(userNotLikeExample).stream()
            .map(UserNotLike::getArticleId).collect(Collectors.toList());

        if (type) {
            return articleList.stream().filter(new Predicate<ArticleForSortDTO>() {

                @Override
                public boolean test(ArticleForSortDTO t) {
                    if (t.getIfAllStick().equals((byte)0)) {
                        return !unlList.contains(t.getArticleId());
                    }
                    return true;
                }
            }).collect(Collectors.toList());

        } else {
            return articleList.stream().filter(new Predicate<ArticleForSortDTO>() {

                @Override
                public boolean test(ArticleForSortDTO t) {
                    if (t.getIfPartStick().equals((byte)0)) {
                        return !unlList.contains(t.getArticleId());
                    }
                    return true;
                }
            }).collect(Collectors.toList());
        }

    }

}
