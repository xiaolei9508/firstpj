package com.cmft.slas.cmuop.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.cmft.slas.cmuop.entity.CmuopEntity;
import com.cmft.slas.cmuop.entity.CmuopEntityType;
import com.cmft.slas.cmuop.mapper.CmuopEntityTypeMapper;
import com.cmft.slas.common.pojo.entity.DictItem;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmft.slas.cmuop.dto.CmuopDictItemDTO;
import com.cmft.slas.cmuop.entity.CmuopDictItem;
import com.cmft.slas.cmuop.mapper.CmuopDictItemMapper;
import com.cmft.slas.cmuop.service.ColumnService;
import com.cmft.slas.cmuop.vo.ColumnVO;
import com.google.common.collect.Lists;
import tk.mybatis.mapper.entity.Example;

@Service
public class ColumnServiceImpl implements ColumnService {

    @Autowired
    private CmuopDictItemMapper cmuopDictItemMapper;

    @Autowired
    private CmuopEntityTypeMapper cmuopEntityTypeMapper;

    @Override
    public List<ColumnVO> getAppColumns(String entityCode) {
//        CmuopDictItemDTO cmuopDictItemDTO = new CmuopDictItemDTO();
//        cmuopDictItemDTO.setDictCode("three_level");
//        cmuopDictItemDTO.setIsDelete((byte)0);
//        List<CmuopDictItem> dictItemList = cmuopDictItemMapper.queryByCondition(cmuopDictItemDTO);
//
//        List<ColumnVO> res = Lists.newArrayList();
//        for (CmuopDictItem cmuopDictItem : dictItemList) {
//            res.add(new ColumnVO().setColumnName(cmuopDictItem.getText()).setColumnType(cmuopDictItem.getValue()));
//        }
//        return res;
        Example typeEg = new Example(CmuopEntityType.class);
        typeEg.createCriteria()
                .andEqualTo("entityCode", entityCode)
                .andEqualTo("isDelete", (byte)0);
        List<CmuopEntityType> typeList = cmuopEntityTypeMapper.selectByExample(typeEg);
        if(CollectionUtils.isEmpty(typeList))
            return null;
        Map<String, Integer> orderMap = typeList.stream()
                .collect(Collectors.toMap(CmuopEntityType::getColumnType, CmuopEntityType::getOrderNum));
        Example entityEg = new Example(CmuopDictItem.class);
        entityEg.createCriteria()
                .andEqualTo("dictCode", "three_level")
                .andIn("value", orderMap.keySet());
        List<CmuopDictItem> dicts = cmuopDictItemMapper.selectByExample(entityEg);
        if(CollectionUtils.isEmpty(dicts))
            return null;
        List<ColumnVO> res = Lists.newArrayList();
        dicts.forEach(item -> res.add(new ColumnVO().setColumnName(item.getText()).setColumnType(item.getValue())));
        res.sort(Comparator.comparing(a -> orderMap.get(a.getColumnType())));
        return res;
    }

}
