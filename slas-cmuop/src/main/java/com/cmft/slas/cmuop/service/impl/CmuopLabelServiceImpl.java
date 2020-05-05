package com.cmft.slas.cmuop.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.cmft.slas.cmuop.common.constant.RedisConstant;
import com.cmft.slas.cmuop.common.utils.CastListUtil;
import com.cmft.slas.cmuop.dto.CmuopEntityDTO;
import com.cmft.slas.cmuop.dto.CmuopLabelDTO;
import com.cmft.slas.cmuop.entity.CmuopLabel;
import com.cmft.slas.cmuop.mapper.CmuopEntityMapper;
import com.cmft.slas.cmuop.mapper.CmuopLabelMapper;
import com.cmft.slas.cmuop.service.CmuopLabelService;
import com.cmft.slas.common.pojo.Page;
import com.cmft.slas.common.utils.BeanMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;

@Service
public class CmuopLabelServiceImpl implements CmuopLabelService {

    @Autowired
    private CmuopLabelMapper cmuopLabelMapper;

    @Autowired
    private CmuopEntityMapper cmuopEntityMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public int addCmuopLabel(CmuopLabelDTO cmuopLabelDTO) {
        cleanLabelData();
        return cmuopLabelMapper.insertSelective(BeanMapper.map(cmuopLabelDTO, CmuopLabel.class));
    }

    @Override
    public int updateCmuopLabel(CmuopLabelDTO cmuopLabelDTO) {
        cleanLabelData();
        return cmuopLabelMapper.updateSelective(cmuopLabelDTO);
    }

    @Override
    public int deleteCmuopLabel(Long id) {
        cleanLabelData();
        Example example = new Example(CmuopLabel.class);
        example.createCriteria().andEqualTo("labelId", id);
        return cmuopLabelMapper.deleteByExample(example);
    }

    @Override
    public CmuopLabelDTO queryById(Long id) {
        return BeanMapper.copy(cmuopLabelMapper.selectByPrimaryKey(id), CmuopLabelDTO.class);
    }

    @Override
    public long countByCondition(CmuopLabelDTO cmuopLabelDTO) {
        return cmuopLabelMapper.countByCondition(cmuopLabelDTO);

    }

    @Override
    public PageInfo<CmuopLabelDTO> queryByPage(CmuopLabelDTO cmuopLabelDTO, Page page) {
        if (StringUtils.isBlank(page.getOrderBy())) {
            page.setOrderBy("label_id DESC");
        }
        /*
         * pagehelper分页
         */
        PageHelper.startPage(page);

        List<CmuopLabelDTO> cmuopLabelDTOList =
            BeanMapper.mapList(cmuopLabelMapper.queryByCondition(cmuopLabelDTO), CmuopLabelDTO.class);
        PageInfo<CmuopLabelDTO> pageInfo = new PageInfo<>(cmuopLabelDTOList);

        return pageInfo;
    }

    /**
     * 领导人标签查询
     * 
     * @param cmuopLabelDTOIN
     * @return
     */
    @Override
    public List<CmuopLabelDTO> queryLabelListByCondition(CmuopLabelDTO cmuopLabelDTOIN) {
        List<CmuopLabelDTO> cmuopLabelDTOList = CastListUtil.castList(
            redisTemplate.opsForValue()
                .get(String.format(RedisConstant.LABEL_DATA_LABELTYPE_KEY, cmuopLabelDTOIN.getLabelType())),
            CmuopLabelDTO.class);
        if (CollectionUtils.isEmpty(cmuopLabelDTOList)) {
            cmuopLabelDTOList =
                BeanMapper.mapList(cmuopLabelMapper.queryByCondition(cmuopLabelDTOIN), CmuopLabelDTO.class);
            for (CmuopLabelDTO cmuopLabelDTO : cmuopLabelDTOList) {
                String searchWords = cmuopLabelDTO.getSearchWords();
                if (StringUtils.isNotBlank(searchWords)) {
                    String[] split = searchWords.split(",");
                    cmuopLabelDTO.setSearchWordList(Arrays.asList(split));
                }
                String belongedEntityId = cmuopLabelDTO.getBelongedEntityId();
                if (StringUtils.isNotBlank(belongedEntityId)) {
                    CmuopEntityDTO cmuopEntityDTO = new CmuopEntityDTO();
                    cmuopEntityDTO.setEntityCode(belongedEntityId);
                    List<CmuopEntityDTO> cmuopEntityDTOList =
                        BeanMapper.mapList(cmuopEntityMapper.queryByCondition(cmuopEntityDTO), CmuopEntityDTO.class);
                    if (CollectionUtils.isNotEmpty(cmuopEntityDTOList)) {
                        cmuopLabelDTO.setBelongedEntityName(cmuopEntityDTOList.get(0).getEntityName());
                    }
                }
            }
            redisTemplate.opsForValue().set(
                String.format(RedisConstant.LABEL_DATA_LABELTYPE_KEY, cmuopLabelDTOIN.getLabelType()),
                cmuopLabelDTOList);
        }
        return cmuopLabelDTOList;
    }

    @Override
    public CmuopLabelDTO queryCmuopLabelById(Long id) {
        return BeanMapper.map(cmuopLabelMapper.queryCmuopLabelById(id), CmuopLabelDTO.class);
    }

    @Override
    public CmuopLabelDTO queryByLabelId(Long labelId) {
        CmuopLabelDTO cmuopLabelDTO = null;
        Example example = new Example(CmuopLabel.class);
        example.createCriteria().andEqualTo("labelId", labelId);
        List<CmuopLabel> cmuopLabels = cmuopLabelMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(cmuopLabels)) {
            cmuopLabelDTO = BeanMapper.map(cmuopLabels.get(0), CmuopLabelDTO.class);
        }
        return cmuopLabelDTO;
    }

    @Override
    public List<CmuopLabelDTO> queryByCondition(CmuopLabelDTO cmuopLabelDTO) {
        List<CmuopLabelDTO> cmuopLabelDTOList =
            BeanMapper.mapList(cmuopLabelMapper.queryByCondition(cmuopLabelDTO), CmuopLabelDTO.class);
        for (CmuopLabelDTO cmuopLabelDTO_01 : cmuopLabelDTOList) {
            setLabel(cmuopLabelDTO_01);
        }
        return cmuopLabelDTOList;
    }

    private void setLabel(CmuopLabelDTO cmuopLabelDTO) {
        String belongedEntityId = cmuopLabelDTO.getBelongedEntityId();
        if (StringUtils.isNotBlank(belongedEntityId)) {
            String belongedEntityName = cmuopEntityMapper.queryEntityNameByEntityCode(belongedEntityId);
            cmuopLabelDTO.setBelongedEntityName(belongedEntityName);
        }
    }

    private void cleanLabelData() {
        Set<String> keys = redisTemplate.keys(String.format(RedisConstant.LABEL_DATA_LABELTYPE_KEY, "*"));
        if (null != keys) {
            for (String key : keys) {
                redisTemplate.delete(key);
            }
        }
    }
}
