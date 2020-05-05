package com.cmft.slas.cmuop.service.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.cmft.slas.cmuop.common.constant.RedisConstant;
import com.cmft.slas.cmuop.common.utils.CastListUtil;
import com.cmft.slas.cmuop.dto.CmuopDictItemDTO;
import com.cmft.slas.cmuop.entity.CmuopDictItem;
import com.cmft.slas.cmuop.mapper.CmuopDictItemMapper;
import com.cmft.slas.cmuop.service.CmuopDictItemService;
import com.cmft.slas.common.pojo.Page;
import com.cmft.slas.common.utils.BeanMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class CmuopDictItemServiceImpl implements CmuopDictItemService {

    @Autowired
    private CmuopDictItemMapper cmuopDictItemMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public int addCmuopDictItem(CmuopDictItemDTO cmuopDictItemDTO) {
        cleanRedisData();
        return cmuopDictItemMapper.insertSelective(BeanMapper.map(cmuopDictItemDTO, CmuopDictItem.class));
    }

    @Override
    public int updateCmuopDictItem(CmuopDictItemDTO cmuopDictItemDTO) {
        cleanRedisData();
        return cmuopDictItemMapper.updateByPrimaryKeySelective(BeanMapper.map(cmuopDictItemDTO, CmuopDictItem.class));
    }

    @Override
    public int deleteCmuopDictItem(Long id) {
        cleanRedisData();
        return cmuopDictItemMapper.deleteByPrimaryKey(id);
    }

    @Override
    public CmuopDictItemDTO queryById(Long id) {
        return BeanMapper.copy(cmuopDictItemMapper.selectByPrimaryKey(id), CmuopDictItemDTO.class);
    }

    @Override
    public long countByCondition(CmuopDictItemDTO cmuopDictItemDTO) {
        return cmuopDictItemMapper.countByCondition(cmuopDictItemDTO);

    }

    @Override
    public List<CmuopDictItemDTO> queryByCondition(CmuopDictItemDTO cmuopDictItemDTO) {
        List<CmuopDictItemDTO> dictItemDTOList = CastListUtil
            .castList(redisTemplate.opsForValue().get(RedisConstant.DICTITEM_LIST_KEY), CmuopDictItemDTO.class);
        if (CollectionUtils.isEmpty(dictItemDTOList)) {
            dictItemDTOList =
                BeanMapper.mapList(cmuopDictItemMapper.queryByCondition(cmuopDictItemDTO), CmuopDictItemDTO.class);
            redisTemplate.opsForValue().set(RedisConstant.DICTITEM_LIST_KEY, dictItemDTOList);
        }
        return dictItemDTOList;
    }

    @Override
    public List<CmuopDictItemDTO> queryByConditionByDictCode(CmuopDictItemDTO cmuopDictItemDTO) {
        List<CmuopDictItemDTO> dictItemDTOList = CastListUtil.castList(
            redisTemplate.opsForValue()
                .get(String.format(RedisConstant.DICTITEM_DATA_DICTCODE_KEY, cmuopDictItemDTO.getDictCode())),
            CmuopDictItemDTO.class);
        if (CollectionUtils.isEmpty(dictItemDTOList)) {
            dictItemDTOList =
                BeanMapper.mapList(cmuopDictItemMapper.queryByCondition(cmuopDictItemDTO), CmuopDictItemDTO.class);
            redisTemplate.opsForValue().set(
                String.format(RedisConstant.DICTITEM_DATA_DICTCODE_KEY, cmuopDictItemDTO.getDictCode()),
                dictItemDTOList);
        }
        return dictItemDTOList;
    }

    @Override
    public PageInfo<CmuopDictItemDTO> queryByPage(CmuopDictItemDTO cmuopDictItemDTO, Page page) {
        if (StringUtils.isBlank(page.getOrderBy())) {
            page.setOrderBy("t_dict_item_id DESC");
        }
        /*
         * pagehelper分页
         */
        PageHelper.startPage(page);

        List<CmuopDictItemDTO> cmuopDictItemDTOList =
            BeanMapper.mapList(cmuopDictItemMapper.queryByCondition(cmuopDictItemDTO), CmuopDictItemDTO.class);

        return new PageInfo<>(cmuopDictItemDTOList);
    }

    @Override
    public Boolean cleanDictItemByDictCode(String dictCode) {
        return redisTemplate.delete(String.format(RedisConstant.DICTITEM_DATA_DICTCODE_KEY, dictCode));
    }

    // 清空redis中对应的数据
    private void cleanRedisData() {
        Set<String> keys = redisTemplate.keys(String.format(RedisConstant.DICTITEM_DATA_DICTCODE_KEY, "*"));
        if (null != keys) {
            for (String key : keys) {
                redisTemplate.delete(key);
            }
        }
    }

    @Override
    public String cleanRedisDataByRedisKey(String redisKey) {
        Boolean hasKey = redisTemplate.hasKey(redisKey);
        if (hasKey) {
            hasKey = redisTemplate.delete(redisKey);
            return hasKey ? "删除成功" : "删除失败";
        } else {
            return "redis中不存在".concat(redisKey);
        }
    }
}
