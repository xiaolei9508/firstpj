package com.cmft.slas.cmuop.service.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.cmft.slas.cmuop.common.constant.RedisConstant;
import com.cmft.slas.cmuop.common.utils.CastListUtil;
import com.cmft.slas.cmuop.dto.FieldDTO;
import com.cmft.slas.cmuop.entity.Field;
import com.cmft.slas.cmuop.mapper.FieldMapper;
import com.cmft.slas.cmuop.service.FieldService;
import com.cmft.slas.common.pojo.Page;
import com.cmft.slas.common.utils.BeanMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class FieldServiceImpl implements FieldService {

    @Autowired
    private FieldMapper fieldMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public int addField(FieldDTO fieldDTO) {
        cleanFieldList();
        return fieldMapper.insertSelective(BeanMapper.map(fieldDTO, Field.class));
    }

    @Override
    public int updateField(FieldDTO fieldDTO) {
        cleanFieldList();
        return fieldMapper.updateByPrimaryKeySelective(BeanMapper.map(fieldDTO, Field.class));
    }

    @Override
    public int deleteField(Long id) {
        cleanFieldList();
        return fieldMapper.deleteByPrimaryKey(id);
    }

    @Override
    public FieldDTO queryById(Long id) {
        FieldDTO fieldDTO = BeanMapper.copy(fieldMapper.selectByPrimaryKey(id), FieldDTO.class);
        String searchWords = fieldDTO.getSearchWords();
        if (StringUtils.isNotBlank(searchWords)) {
            String[] strings = searchWords.split(",");
            fieldDTO.setSearchWordList(Arrays.asList(strings));
        }
        return fieldDTO;
    }

    @Override
    public long countByCondition(FieldDTO fieldDTO) {
        return fieldMapper.countByCondition(fieldDTO);

    }

    @Override
    public List<FieldDTO> queryByCondition(FieldDTO fieldDTOIN) {
        List<FieldDTO> fieldDTOList =
            CastListUtil.castList(redisTemplate.opsForValue().get(RedisConstant.FIELD_LIST_KEY), FieldDTO.class);
        if (CollectionUtils.isEmpty(fieldDTOList)) {
            fieldDTOList = BeanMapper.mapList(fieldMapper.queryByCondition(fieldDTOIN), FieldDTO.class);
            for (FieldDTO fieldDTO : fieldDTOList) {
                String searchWords = fieldDTO.getSearchWords();
                if (StringUtils.isNotBlank(searchWords)) {
                    String[] split = searchWords.split(",");
                    fieldDTO.setSearchWordList(Arrays.asList(split));
                }
            }
            redisTemplate.opsForValue().set(RedisConstant.FIELD_LIST_KEY, fieldDTOList);
        }
        return fieldDTOList;
    }

    @Override
    public PageInfo<FieldDTO> queryByPage(FieldDTO fieldDTO, Page page) {
        if (StringUtils.isBlank(page.getOrderBy())) {
            page.setOrderBy("field_id DESC");
        }
        /*
         * pagehelper分页
         */
        PageHelper.startPage(page);

        List<FieldDTO> fieldDTOList = BeanMapper.mapList(fieldMapper.queryByCondition(fieldDTO), FieldDTO.class);
        PageInfo<FieldDTO> pageInfo = new PageInfo<>(fieldDTOList);

        return pageInfo;
    }

    @Override
    public List<FieldDTO> getFieldList() {
        List<FieldDTO> fieldDTOList = BeanMapper.mapList(fieldMapper.queryFieldList(), FieldDTO.class);
        for (FieldDTO fieldDTO : fieldDTOList) {
            String searchWords = fieldDTO.getSearchWords();
            if (StringUtils.isNotBlank(searchWords)) {
                String[] split = searchWords.split(",");
                fieldDTO.setSearchWordList(Arrays.asList(split));
            }
        }
        return fieldDTOList;
    }

    private void cleanFieldList() {
        redisTemplate.delete(RedisConstant.FIELD_LIST_KEY);
    }
}
