package com.cmft.slas.cmuop.processor.processorImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.cmft.slas.cmuop.common.constant.ColumnTypes;
import com.cmft.slas.cmuop.common.constant.FakeEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cmft.slas.cmuop.entity.ArticleRelatedEntity;
import com.cmft.slas.cmuop.entity.ArticleType;
import com.cmft.slas.cmuop.entity.CmuopDictItem;
import com.cmft.slas.cmuop.entity.CmuopEntity;
import com.cmft.slas.cmuop.mapper.ArticleMapper;
import com.cmft.slas.cmuop.mapper.ArticleRelatedEntityMapper;
import com.cmft.slas.cmuop.mapper.ArticleTypeMapper;
import com.cmft.slas.cmuop.mapper.CmuopDictItemMapper;
import com.cmft.slas.cmuop.mapper.CmuopEntityMapper;
import com.cmft.slas.cmuop.processor.ArticlePreviewProcessor;
import com.cmft.slas.cmuop.vo.AllArticlePreview;
import com.cmft.slas.cmuop.vo.AppArticlePreview;
import com.cmft.slas.cmuop.vo.ArticleDetail;
import com.cmft.slas.cmuop.vo.ArticlePreview;

import tk.mybatis.mapper.entity.Example;

/**
 * @Author liurp001
 * @Since 2019/12/30
 */
@Component
public class ArticleProcessorImpl implements ArticlePreviewProcessor {

    @Autowired
    private ArticleRelatedEntityMapper articleRelatedEntityMapper;

    @Autowired
    private CmuopEntityMapper cmuopEntityMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleTypeMapper articleTypeMapper;

    @Autowired
    private CmuopDictItemMapper cmuopDictItemMapper;

    /**
     * input param only contains article_id
     *
     * @param articlePreview
     */
    @Override
    public void processPreview(List<? extends ArticlePreview> articlePreview) {
        if(CollectionUtils.isNotEmpty(articlePreview)){
            ArticlePreview sample = articlePreview.get(0);
            if(sample instanceof AppArticlePreview){
                processAppPreview(articlePreview);
            }else{
                processWebPreview(articlePreview);
            }
        }
    }

    private void processWebPreview(List<? extends ArticlePreview> articlePreview){
        processEntities(articlePreview);
        processArticleThirdLevelType(articlePreview);
    }

    private void processAppPreview(List<? extends ArticlePreview> articlePreview){
        processAppEntities(articlePreview);

    }

    private void processEntities(List<? extends ArticlePreview> articlePreview) {
        // article map: articleId -> ArticlePreview
        Map<String, ArticlePreview> previewMap = articlePreview.stream()
                .collect(Collectors.toMap(ArticlePreview::getArticleId, value -> value));
        Example articleRelatedEntityEg = new Example(ArticleRelatedEntity.class);
        articleRelatedEntityEg.createCriteria()
                .andIn("articleId", previewMap.keySet())
                .andNotEqualTo("entityCode", FakeEntity.ZHAOWEN.getCode())
                .andNotEqualTo("columnType", ColumnTypes.RECOMMEND.getValue());
        List<ArticleRelatedEntity> relatedList = articleRelatedEntityMapper.selectByExample(articleRelatedEntityEg);
        if (CollectionUtils.isEmpty(relatedList))
            return;
        // relation map: articleId -> List<Entities>
        Map<String, List<ArticleRelatedEntity>> articleToEntityMap = relatedList.stream()
                .collect(Collectors.groupingBy(ArticleRelatedEntity::getArticleId));

        List<String> entityCodes = relatedList.stream().map(ArticleRelatedEntity::getEntityCode).distinct().collect(Collectors.toList());
        Example entityEg = new Example(CmuopEntity.class);
        entityEg.createCriteria().andIn("entityCode", entityCodes);
        List<CmuopEntity> entities = cmuopEntityMapper.selectByExample(entityEg);
        // entity map: entityCode -> entityName
        Map<String, String> entityMap = entities.stream().collect(Collectors.toMap(CmuopEntity::getEntityCode, CmuopEntity::getEntityName));

        // append entity to preview
        previewMap.forEach((key, item) ->{
            List<ArticleRelatedEntity> entityList = articleToEntityMap.get(key);
            if(CollectionUtils.isNotEmpty(entityList)){
                List<String> codeList = entityList.stream().map(ArticleRelatedEntity::getEntityCode).collect(Collectors.toList());
                List<String> entites = new ArrayList<>();
                codeList.forEach(code -> {
                    String name = entityMap.get(code);
                    if(StringUtils.isNotBlank(name))
                        entites.add(name);
                });
                item.setEntities(entites.stream().distinct().collect(Collectors.toList()));
            }
        });
    }

    private void processAppEntities(List<? extends ArticlePreview> appArticlePreview) {
        List<String> list = appArticlePreview.stream().map(ArticlePreview::getArticleId).distinct().collect(Collectors.toList());
        List<AllArticlePreview> appList = articleMapper.getAllArticleViaJoin(null, null, list, null,null, null, null, null);
        Example articleRelatedEntityEg = new Example(ArticleRelatedEntity.class);
        articleRelatedEntityEg.createCriteria()
                .andIn("articleId", list)
                .andNotEqualTo("entityCode", FakeEntity.ZHAOWEN.getCode())
                .andNotEqualTo("columnType", ColumnTypes.RECOMMEND.getValue());
        List<ArticleRelatedEntity> entityList = articleRelatedEntityMapper.selectByExample(articleRelatedEntityEg);
        // entity map
        Map<String, List<ArticleRelatedEntity>> areMap =
            entityList.stream().collect(Collectors.groupingBy(ArticleRelatedEntity::getArticleId));
        Map<String, AllArticlePreview> spMap =
            appList.stream().collect(Collectors.toMap(AllArticlePreview::getArticleId, value -> value));
        for (ArticlePreview article : appArticlePreview) {
            article.setViewCount(
                spMap.get(article.getArticleId()) == null ? 0 : spMap.get(article.getArticleId()).getViewCount());
            List<ArticleRelatedEntity> areList = areMap.get(article.getArticleId());
            if (CollectionUtils.isEmpty(areList))
                continue;
            String entityCode = areList.get(0).getEntityCode();
            CmuopEntity ce = new CmuopEntity();
            ce.setEntityCode(entityCode);
            ce = cmuopEntityMapper.selectOne(ce);
            if (ce != null) {
                ((AppArticlePreview)article).setEntityCode(ce.getEntityCode());
                ((AppArticlePreview)article).setEntityName(ce.getEntityName());
            }

        }

    }

    private void processArticleThirdLevelType(List<? extends ArticlePreview> articlePreview) {
        // preview map: articleId -> ArticlePreview
        Map<String, ArticlePreview> previewMap = articlePreview.stream().collect(Collectors.toMap(ArticlePreview::getArticleId, value -> value));
        Example thirdEg = new Example(ArticleType.class);
        thirdEg.createCriteria().andIn("articleId", previewMap.keySet());
        List<ArticleType> thirdLevelCode = articleTypeMapper.selectByExample(thirdEg);
        if(CollectionUtils.isEmpty(thirdLevelCode))
            return;
        // third level code map: articleId -> List<ArticleType>
        Map<String, List<ArticleType>> thirdLevelMap = thirdLevelCode.stream().collect(Collectors.groupingBy(ArticleType::getArticleId));

        List<String> codeList = thirdLevelCode.stream().map(ArticleType::getTypeId).distinct().collect(Collectors.toList());

        Example dictEg = new Example(CmuopDictItem.class);
        dictEg.createCriteria().andEqualTo("dictCode", "three_level").andIn("value", codeList);
        List<CmuopDictItem> thirdLevelName = cmuopDictItemMapper.selectByExample(dictEg);
        // third level name map: dict value -> dict text
        Map<String, String> thirdLevelNameMap = thirdLevelName.stream().collect(Collectors.toMap(CmuopDictItem::getValue, CmuopDictItem::getText));

        previewMap.forEach((key, item) ->{
            List<ArticleType> typeList = thirdLevelMap.get(key);
            if(CollectionUtils.isNotEmpty(typeList)){
                List<String> types = typeList.stream().map(ArticleType::getTypeId).collect(Collectors.toList());
                List<String> typeNames = new ArrayList<>();
                types.forEach(code ->{
                    String name = thirdLevelNameMap.get(code);
                    if(StringUtils.isNotBlank(name)){
                        typeNames.add(name);
                    }
                });
                item.setTypeOfContent(typeNames);
            }

        });
    }


    @Override
    public void processDetail(ArticleDetail articleDetail) {

    }
}
