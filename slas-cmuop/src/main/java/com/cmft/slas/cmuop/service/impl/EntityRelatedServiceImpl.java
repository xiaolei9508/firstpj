package com.cmft.slas.cmuop.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.cmft.slas.cmuop.common.constant.RedisConstant;
import com.cmft.slas.cmuop.dto.EntityRelatedDTO;
import com.cmft.slas.cmuop.entity.EntityRelated;
import com.cmft.slas.cmuop.mapper.CmuopEntityMapper;
import com.cmft.slas.cmuop.mapper.EntityRelatedMapper;
import com.cmft.slas.cmuop.service.EntityRelatedService;
import com.cmft.slas.common.pojo.Page;
import com.cmft.slas.common.utils.BeanMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class EntityRelatedServiceImpl implements EntityRelatedService {
    @Autowired
    private EntityRelatedMapper entityRelatedMapper;

    @Autowired
    private CmuopEntityMapper cmuopEntityMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public PageInfo<EntityRelatedDTO> getEntityRelatedList(EntityRelatedDTO entityRelatedDTO, Page page) {
        if (StringUtils.isBlank(page.getOrderBy())) {
            page.setOrderBy("entity_related_id DESC");
        }
        /*
         * pagehelper分页
         */
        PageHelper.startPage(page);

        List<EntityRelatedDTO> entityRelatedDTOList =
            BeanMapper.mapList(entityRelatedMapper.queryByCondition(entityRelatedDTO), EntityRelatedDTO.class);
        PageInfo<EntityRelatedDTO> pageInfo = new PageInfo<>(entityRelatedDTOList);
        for (EntityRelatedDTO entityRelated : entityRelatedDTOList) {
            entityRelated.setEntityName(cmuopEntityMapper.queryEntityNameByEntityCode(entityRelated.getEntityCode()));
            entityRelated.setRelatedEntityName(
                cmuopEntityMapper.queryEntityNameByEntityCode(entityRelated.getRelatedEntityCode()));
        }

        return pageInfo;
    }

    @Override
    public int addEntityRelated(EntityRelatedDTO entityRelatedDTO) {
        cleanEntityRelated();
        return entityRelatedMapper.insertSelective(BeanMapper.map(entityRelatedDTO, EntityRelated.class));
    }

    @Override
    public Long countByCondition(EntityRelatedDTO entityRelatedDTO) {
        return entityRelatedMapper.countByCondition(entityRelatedDTO);
    }

    @Override
    public int deleteEntityRelated(Long id) {
        cleanEntityRelated();
        return entityRelatedMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateEntityRelated(EntityRelatedDTO entityRelatedDTO) {
        cleanEntityRelated();
        return entityRelatedMapper.updateByPrimaryKeySelective(BeanMapper.map(entityRelatedDTO, EntityRelated.class));
    }

    @Override
    public EntityRelatedDTO queryEntityRelatedById(Long id) {
        EntityRelatedDTO entityRelatedDTO =
            BeanMapper.map(entityRelatedMapper.selectByPrimaryKey(id), EntityRelatedDTO.class);
        String entityCode = entityRelatedDTO.getEntityCode();
        String relatedEntityCode = entityRelatedDTO.getRelatedEntityCode();
        entityRelatedDTO.setEntityName(cmuopEntityMapper.queryEntityNameByEntityCode(entityCode));
        entityRelatedDTO.setRelatedEntityName(cmuopEntityMapper.queryEntityNameByEntityCode(relatedEntityCode));
        return entityRelatedDTO;
    }

    @Override
    public List<EntityRelatedDTO> queryByCondition(EntityRelatedDTO entityRelatedDTO) {
        List<EntityRelatedDTO> entityRelatedDTOList =
            BeanMapper.mapList(entityRelatedMapper.queryByCondition(entityRelatedDTO), EntityRelatedDTO.class);
        for (EntityRelatedDTO relatedDTO : entityRelatedDTOList) {
            String entityCode = relatedDTO.getEntityCode();
            String relatedEntityCode = relatedDTO.getRelatedEntityCode();
            relatedDTO.setEntityName(cmuopEntityMapper.queryEntityNameByEntityCode(entityCode));
            relatedDTO.setRelatedEntityName(cmuopEntityMapper.queryEntityNameByEntityCode(relatedEntityCode));
        }
        return entityRelatedDTOList;
    }

    private void cleanEntityRelated() {
        redisTemplate.delete(RedisConstant.ENTITY_RELATED_LIST_KEY);
    }
}
