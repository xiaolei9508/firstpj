package com.cmft.slas.cmuop.service.impl;

import com.cmft.slas.cmuop.common.constant.ColumnTypes;
import com.cmft.slas.cmuop.common.constant.FakeEntity;
import com.cmft.slas.cmuop.dto.CmuopEntityDTO;
import com.cmft.slas.cmuop.entity.ArticleRelatedEntity;
import com.cmft.slas.cmuop.service.CmuopEntityService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmft.slas.cmuop.dto.ArticleRelatedEntityDTO;
import com.cmft.slas.cmuop.mapper.ArticleRelatedEntityMapper;
import com.cmft.slas.cmuop.service.ArticleRelatedEntityService;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class ArticleRelatedEntityServiceImpl implements ArticleRelatedEntityService {

    @Autowired
    ArticleRelatedEntityMapper articleRelatedEntityMapper;

    @Autowired
    CmuopEntityService cmuopEntityService;

    @Override
    public Long countByCondition(ArticleRelatedEntityDTO articleRelatedEntityDTO) {
        return articleRelatedEntityMapper.countByCondition(articleRelatedEntityDTO);
    }

    @Override
    public void batchProcessEntityWithRecommend(String articleId, Boolean ifRecommend) {
        if(StringUtils.isBlank(articleId) && ifRecommend == null){
            return;
        }
        Example example = new Example(ArticleRelatedEntity.class);
        Example.Criteria criteria = example.createCriteria()
                .andEqualTo("articleId", articleId)
                .andEqualTo("columnType", "0");
        articleRelatedEntityMapper.deleteByExample(example);
        if(ifRecommend) {
            CmuopEntityDTO cmuopEntityDTO = new CmuopEntityDTO();
            cmuopEntityDTO.setIfCmuop((byte)1);
            cmuopEntityDTO.setIsDelete((byte)0);
            List<CmuopEntityDTO> cmuopEntityDTOList = cmuopEntityService.queryByCondition(cmuopEntityDTO);
            for(CmuopEntityDTO entity : cmuopEntityDTOList) {
                ArticleRelatedEntity articleRelated = new ArticleRelatedEntity();
                articleRelated.setArticleId(articleId);
                articleRelated.setEntityCode(entity.getEntityCode());
                articleRelated.setColumnType("0");
                articleRelated.setOrderNum(99);
                articleRelatedEntityMapper.insertSelective(articleRelated);
            }
            ArticleRelatedEntity articleRelated = new ArticleRelatedEntity();
            articleRelated.setArticleId(articleId);
            articleRelated.setEntityCode("000");
            articleRelated.setColumnType("0");
            articleRelated.setOrderNum(99);
            articleRelatedEntityMapper.insertSelective(articleRelated);
        }

    }

    @Override
    public boolean checkIfEntityExists(String articleId) {
        Example example = new Example(ArticleRelatedEntity.class);
        example.createCriteria()
                .andEqualTo("articleId", articleId)
                .andNotEqualTo("columnType", ColumnTypes.RECOMMEND.getValue())
                .andNotEqualTo("entityCode", FakeEntity.ZHAOWEN.getCode())
                .andNotEqualTo("columnType", ColumnTypes.INDUSTRY.getValue());
        return CollectionUtils.isNotEmpty(articleRelatedEntityMapper.selectByExample(example));
    }

    @Override
    public void processAllEntityWithRecommend(String articleId, Boolean ifRecommend) {
        if(StringUtils.isBlank(articleId) && ifRecommend == null){
            return;
        }
        Example example = new Example(ArticleRelatedEntity.class);
        Example.Criteria criteria = example.createCriteria()
                .andEqualTo("articleId", articleId);
        articleRelatedEntityMapper.deleteByExample(example);
    }

}
