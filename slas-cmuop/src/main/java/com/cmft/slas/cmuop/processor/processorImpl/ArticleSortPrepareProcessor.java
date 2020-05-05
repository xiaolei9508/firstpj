package com.cmft.slas.cmuop.processor.processorImpl;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cmft.slas.cmuop.common.utils.TimeUtil;
import com.cmft.slas.cmuop.dto.ArticleForSortDTO;
import com.cmft.slas.cmuop.dto.ArticleLabelDTO;
import com.cmft.slas.cmuop.dto.ArticleRelatedEntityDTO;
import com.cmft.slas.cmuop.dto.CmuopLabelDTO;
import com.cmft.slas.cmuop.dto.UserEntityDTO;
import com.cmft.slas.cmuop.entity.ArticleLabel;
import com.cmft.slas.cmuop.entity.ArticleRelatedEntity;
import com.cmft.slas.cmuop.entity.CmuopLabel;
import com.cmft.slas.cmuop.entity.UserEntity;
import com.cmft.slas.cmuop.entity.UserView;
import com.cmft.slas.cmuop.mapper.ArticleLabelMapper;
import com.cmft.slas.cmuop.mapper.ArticleRelatedEntityMapper;
import com.cmft.slas.cmuop.mapper.CmuopLabelMapper;
import com.cmft.slas.cmuop.mapper.UserEntityMapper;
import com.cmft.slas.cmuop.mapper.UserViewMapper;
import com.cmft.slas.cmuop.processor.ArticleSortProcessor;
import com.cmft.slas.cmuop.vo.InfoStatVO;
import com.google.common.collect.Lists;

@Component
public class ArticleSortPrepareProcessor implements ArticleSortProcessor {

    @Autowired
    private ArticleRelatedEntityMapper articleRelatedEntityMapper;

    @Autowired
    private UserViewMapper userViewMapper;

    @Autowired
    private ArticleLabelMapper articleLabelMapper;

    @Autowired
    private UserEntityMapper userEntityMapper;

    @Autowired
    private CmuopLabelMapper cmuopLabelMapper;

    @Override
    public void process(List<ArticleForSortDTO> articleList) {
        for (ArticleForSortDTO article : articleList) {
            process(article);
        }

    }

    @Override
    public void process(List<ArticleForSortDTO> articleList, String uid) {
        List<String> entityList = getEntityListByUid(uid);

        List<String> articleIdList =
            articleList.stream().map(ArticleForSortDTO::getArticleId).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(articleIdList)) {
            return;
        }

//        Map<String, List<ArticleRelatedEntity>> areMap =
//            articleRelatedEntityMapper.selectArticleRelatedEntityListByArticleId(articleIdList).stream()
//                .collect(Collectors.groupingBy(ArticleRelatedEntity::getArticleId));

        Map<String, List<UserView>> uvMap = userViewMapper.selectUserViewListByArticleIdList(articleIdList, uid)
            .stream().collect(Collectors.groupingBy(UserView::getArticleId));

        for (ArticleForSortDTO article : articleList) {
            if (article == null) {
                continue;
            }
            String articleId = article.getArticleId();
            
//            if (CollectionUtils.isEmpty(areMap.get(articleId))) {
//                article.setIfRelatedEntity(false);
//                article.setIfIndustry(false);
//            } else {
//                List<String> areList = areMap.get(articleId).stream().filter(new Predicate<ArticleRelatedEntity>() {
//                    @Override
//                    public boolean test(ArticleRelatedEntity t) {
//                        return !"3".equals(t.getColumnType());
//                    }
//
//                }).map(ArticleRelatedEntity::getEntityCode).collect(Collectors.toList());
//                article.setRelatedEntities(areList);
//                article.setIfRelatedEntity(contain(areList,
//                        entityList));
//                List<ArticleRelatedEntity> tempList =
//                    areMap.get(articleId).stream().filter(new Predicate<ArticleRelatedEntity>() {
//
//                    @Override
//                    public boolean test(ArticleRelatedEntity t) {
//                        return "3".equals(t.getColumnType());
//                    }
//
//                    }).collect(Collectors.toList());
//                article.setIfIndustry(!CollectionUtils.isEmpty(tempList));
//            }

            if (CollectionUtils.isEmpty(uvMap.get(articleId))) {
                article.setIfRead(false);
            } else {
                article.setIfRead(true);
            }

            // handle timeStep
            Integer timeInterval = TimeUtil.getTimeIntervalInMill(article.getPubTime());
            if (timeInterval < 1) {
                article.setTimeStep(ArticleForSortDTO.TimeStep.LEVEL6);
            }else if (timeInterval >= 1 && timeInterval < 2) {
                article.setTimeStep(ArticleForSortDTO.TimeStep.LEVEL5);
            }else if (timeInterval >= 2 && timeInterval < 3) {
                article.setTimeStep(ArticleForSortDTO.TimeStep.LEVEL4);
            }else if (timeInterval >= 3 && timeInterval < 5) {
                article.setTimeStep(ArticleForSortDTO.TimeStep.LEVEL3);
            }else if (timeInterval >= 5 && timeInterval < 7) {
                article.setTimeStep(ArticleForSortDTO.TimeStep.LEVEL2);
            }else if (timeInterval >= 7 && timeInterval < 10) {
                article.setTimeStep(ArticleForSortDTO.TimeStep.LEVEL1);
            }else {
                article.setTimeStep(ArticleForSortDTO.TimeStep.LEVEL0);
            }
        }

    }

    private List<String> getEntityListByUid(String uid) {
        UserEntityDTO userEntityDTO = new UserEntityDTO();
        userEntityDTO.setUid(uid);
        userEntityDTO.setIsDelete((byte)0);
        List<UserEntity> ueList = userEntityMapper.queryByCondition(userEntityDTO);
        List<String> entityList = Lists.newArrayList();
        for (UserEntity userEntity : ueList) {
            entityList.add(userEntity.getEntityCode());
        }
        return entityList;
    }

    private Boolean contain(List<String> source, List<String> test) {
        for (String str : test) {
            if (source.contains(str)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void process(List<ArticleForSortDTO> articleList, String uid, String columnType) {

    }

    @Override
    public void process(ArticleForSortDTO article) {
        if (article == null) {
            return;
        }
        String articleId = article.getArticleId();

        CmuopLabelDTO clDTO = new CmuopLabelDTO();
        clDTO.setLabelType("event_label");
        clDTO.setIsDelete((byte)0);
        List<String> eventLabelList = cmuopLabelMapper.queryByCondition(clDTO).stream().map(CmuopLabel::getLabelCode)
            .collect(Collectors.toList());

        ArticleLabelDTO alDTO = new ArticleLabelDTO();
        alDTO.setArticleId(articleId);
        alDTO.setIsDelete((byte)0);
        List<ArticleLabel> alList = articleLabelMapper.queryByCondition(alDTO);

        Boolean ifEvent = false;
        Boolean ifManager = false;

        for (ArticleLabel al : alList) {
            if (eventLabelList.contains(al.getLabelId())) {
                ifEvent = true;
            }
            if (al.getLabelType().equals("user")) {
                ifManager = true;
            }
            if (ifEvent && ifManager) {
                break;
            }
        }

        article.setIfEvent(ifEvent);
        article.setIfManager(ifManager);

        ArticleRelatedEntityDTO areDTO = new ArticleRelatedEntityDTO();
        areDTO.setArticleId(articleId).setIsDelete((byte)0);
        List<ArticleRelatedEntity> areList = articleRelatedEntityMapper.selectDistinctEntity(areDTO);
        List<InfoStatVO> res = Lists.newArrayList();
        for (ArticleRelatedEntity are : areList) {
            InfoStatVO isVO = new InfoStatVO().setCode(are.getEntityCode());
            res.add(isVO);
        }
        article.setRelatedEntityList(res);
    }
}
