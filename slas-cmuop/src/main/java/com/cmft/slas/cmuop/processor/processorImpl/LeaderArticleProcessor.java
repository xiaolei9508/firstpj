package com.cmft.slas.cmuop.processor.processorImpl;

import com.cmft.slas.cmuop.common.constant.ColumnTypes;
import com.cmft.slas.cmuop.common.constant.LabelType;
import com.cmft.slas.cmuop.common.constant.LeaderOrder;
import com.cmft.slas.cmuop.dto.ArticleForSortDTO;
import com.cmft.slas.cmuop.entity.Article;
import com.cmft.slas.cmuop.entity.ArticleLabel;
import com.cmft.slas.cmuop.entity.ArticleRelatedEntity;
import com.cmft.slas.cmuop.entity.UserEntity;
import com.cmft.slas.cmuop.mapper.ArticleLabelMapper;
import com.cmft.slas.cmuop.mapper.ArticleMapper;
import com.cmft.slas.cmuop.mapper.ArticleRelatedEntityMapper;
import com.cmft.slas.cmuop.mapper.UserEntityMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class LeaderArticleProcessor {

    @Autowired
    private ArticleRelatedEntityMapper articleRelatedEntityMapper;

    @Autowired
    private UserEntityMapper userEntityMapper;

    @Autowired
    private ArticleLabelMapper articleLabelMapper;

    @Autowired
    private ArticleMapper articleMapper;

    public void updateOrderNum(String uid, String entityType){
        Example userEntityEg = new Example(UserEntity.class);
        Example.Criteria userEntityCri = userEntityEg.createCriteria()
                .andEqualTo("entityCode", entityType)
                .andEqualTo("isDelete", (byte)0);
        if(StringUtils.isNotBlank(uid)){
            userEntityCri.andEqualTo("uid", uid);
        }else{
            userEntityCri.andNotEqualTo("orderNum", LeaderOrder.DEFAULT.getValue());
        }
        List<UserEntity> leader = userEntityMapper.selectByExample(userEntityEg);
        if(CollectionUtils.isEmpty(leader)){
            log.info("无法找到实体 {} 的领导人 {}", entityType, uid);
            return;
        }
        List<ArticleForSortDTO> partSticks = articleMapper.selectColumnStickArticle(ColumnTypes.LEADER.getValue(),
                entityType,
                null,
                null);
        // @TODO is it necessary to update all history articles?
        leader.forEach(user ->{
            Example labelEg = new Example(ArticleLabel.class);
            labelEg.createCriteria()
                    .andEqualTo("labelType", LabelType.USER.getCode())
                    .andEqualTo("labelId", user.getUid())
                    .andEqualTo("isDelete", (byte)0);
            List<ArticleLabel> labels = articleLabelMapper.selectByExample(labelEg);
            if(CollectionUtils.isNotEmpty(labels)){
                List<String> articleIds = labels.stream().map(ArticleLabel::getArticleId).collect(Collectors.toList());
                ArticleRelatedEntity modelArticle = new ArticleRelatedEntity().setOrderNum(user.getOrderNum());
                Example articleEg = new Example(ArticleRelatedEntity.class);
                Example.Criteria articleCriteria = articleEg.createCriteria()
                        .andEqualTo("entityCode", entityType)
                        .andEqualTo("columnType", ColumnTypes.LEADER.getValue())
                        .andEqualTo("isDelete", (byte)0)
                        .andIn("articleId", articleIds);
                if(CollectionUtils.isNotEmpty(partSticks)){
                    articleCriteria.andNotIn("articleId", partSticks.stream().map(ArticleForSortDTO::getArticleId).collect(Collectors.toList()));
                }
                articleRelatedEntityMapper.updateByExampleSelective(modelArticle, articleEg);
            }
        });
    }

    public Integer updateArticleOrderNum(String articleId, String entityCode){
        Example labelEg = new Example(ArticleLabel.class);
        labelEg.createCriteria()
                .andEqualTo("articleId", articleId)
                .andEqualTo("labelType", "user")
                .andEqualTo("isDelete", (byte)0);
        List<ArticleLabel> articleLabels = articleLabelMapper.selectByExample(labelEg);
        if(CollectionUtils.isEmpty(articleLabels))
            return LeaderOrder.DEFAULT.getValue();
        List<String> uids = articleLabels.stream().map(ArticleLabel::getLabelId).distinct().collect(Collectors.toList());
        Example userEntityEg = new Example(UserEntity.class);
        userEntityEg.createCriteria()
                .andIn("uid", uids)
                .andEqualTo("entityCode", entityCode)
                .andEqualTo("isDelete",(byte)0)
                .andNotEqualTo("orderNum", LeaderOrder.DEFAULT.getValue());
        List<UserEntity> userEntities = userEntityMapper.selectByExample(userEntityEg);
        if(CollectionUtils.isEmpty(userEntities))
            return LeaderOrder.DEFAULT.getValue();
        Integer order = userEntities.stream().map(UserEntity::getOrderNum).min(Integer::compareTo).orElse(LeaderOrder.DEFAULT.getValue());
//        ArticleRelatedEntity articleRelatedEntity = new ArticleRelatedEntity().setOrderNum(order);
//        Example areEg = new Example(ArticleRelatedEntity.class);
//        areEg.createCriteria()
//                .andEqualTo("articleId", articleId)
//                .andEqualTo("entityCode", entityCode)
//                .andEqualTo("columnType", ColumnTypes.LEADER.getValue())
//                .andEqualTo("isDelete", (byte)0);
//        articleRelatedEntityMapper.updateByExampleSelective(articleRelatedEntity,areEg);
        return order;
    }
}

