package com.cmft.slas.cmuop.service.impl;

import com.cmft.slas.cmuop.dto.*;
import com.cmft.slas.cmuop.entity.CmuopDictItem;
import com.cmft.slas.cmuop.entity.CmuopEntity;
import com.cmft.slas.cmuop.entity.CmuopEntityType;
import com.cmft.slas.cmuop.mapper.CmuopDictItemMapper;
import com.cmft.slas.cmuop.mapper.CmuopEntityMapper;
import com.cmft.slas.cmuop.mapper.CmuopEntityTypeMapper;
import com.cmft.slas.cmuop.service.CmuopPointViewTypeService;
import com.cmft.slas.cmuop.service.EntityTypeService;
import com.cmft.slas.cmuop.vo.PointViewTypeVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EntityTypeServiceImpl implements EntityTypeService {
    @Autowired
    private CmuopEntityMapper cmuopEntityMapper;

    @Autowired
    private CmuopEntityTypeMapper cmuopEntityTypeMapper;

    @Autowired
    private CmuopDictItemMapper cmuopDictItemMapper;

    @Autowired
    private CmuopPointViewTypeService cmuopPointViewTypeService;

    private final String ZHAOWEN_CODE = "000";
    private final String ZHAOWEN_NAME = "招闻天下";
    private final String COLUMN_TYPE_OF_POINT_INFO = "4";
    private final String POINT_INFO_NAME_FOR_OTHERS = "其他观点";

    @Override
    public PageInfo<CmuopEntityTypeDTO> queryEntityTypePage(String entityName, Integer pageNum, Integer pageSize) {
        CmuopEntityDTO cmuopEntityDTO = new CmuopEntityDTO();
        pageNum = ObjectUtils.firstNonNull(pageNum, 1);
        pageSize = ObjectUtils.firstNonNull(pageSize, 10);
        PageInfo<CmuopEntity> cmuopEntityPageInfo = queryEntityList(entityName, pageNum, pageSize);

        CmuopDictItemDTO cmuopDictItemDTO = new CmuopDictItemDTO();
        cmuopDictItemDTO.setDictCode("field_level");
        List<CmuopDictItem> cmuopDictItemList = cmuopDictItemMapper.queryByCondition(cmuopDictItemDTO);
        Map<String, String> dictMap = cmuopDictItemList.stream().collect(Collectors.toMap(CmuopDictItem::getValue, CmuopDictItem::getText));

        List<CmuopEntityTypeDTO> list = new ArrayList<>();
        for(CmuopEntity entity : cmuopEntityPageInfo.getList()) {
            CmuopEntityTypeDTO dto = new CmuopEntityTypeDTO();
            BeanUtils.copyProperties(entity, dto);
            dto.setEntityTypeVOList(queryEntityTypeListByEntityCode(entity.getEntityCode(), dictMap));
            list.add(dto);
        }
        if(pageNum == 1 && (StringUtils.isBlank(entityName) || ZHAOWEN_NAME.contains(entityName))) {
            CmuopEntityTypeDTO dto = new CmuopEntityTypeDTO();
            dto.setEntityCode(ZHAOWEN_CODE);
            dto.setEntityName(ZHAOWEN_NAME);
            dto.setOrderNum(0);
            dto.setEntityId(0L);
            dto.setEntityTypeVOList(queryEntityTypeListByEntityCode(ZHAOWEN_CODE, dictMap));
            list.add(0,dto);
        }
        PageInfo<CmuopEntityTypeDTO> pageInfo = new PageInfo<>(list);
        pageInfo.setPageNum(cmuopEntityPageInfo.getPageNum());
        pageInfo.setPageSize(cmuopEntityPageInfo.getPageSize());
        pageInfo.setTotal(cmuopEntityPageInfo.getTotal());
        return pageInfo;
    }

    @Override
    public int updateEntityType(CmuopEntityTypeDTO cmuopEntityTypeDTO) {
        int result = 0;
        if(!ZHAOWEN_CODE.equals(cmuopEntityTypeDTO.getEntityCode()) && cmuopEntityTypeDTO.getOrderNum() != null) {
            CmuopEntity cmuopEntity = new CmuopEntity();
            cmuopEntity.setEntityId(cmuopEntityTypeDTO.getEntityId());
            cmuopEntity.setOrderNum(cmuopEntityTypeDTO.getOrderNum());
            result += cmuopEntityMapper.updateByPrimaryKeySelective(cmuopEntity);
        }
        Example example = new Example(CmuopEntityType.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("entityCode", cmuopEntityTypeDTO.getEntityCode());
        cmuopEntityTypeMapper.deleteByExample(example);
        for(CmuopEntityTypeVO vo : cmuopEntityTypeDTO.getEntityTypeVOList()) {
            CmuopEntityType entityType = new CmuopEntityType();
            BeanUtils.copyProperties(vo, entityType);
            entityType.setEntityTypeId(null);
            entityType.setEntityCode(cmuopEntityTypeDTO.getEntityCode());
            entityType.setIsDelete((byte)0);
            result += cmuopEntityTypeMapper.insertSelective(entityType);
        }
        return result;
    }

    @Override
    public int deleteEntityType(CmuopEntityTypeDTO cmuopEntityTypeDTO) {
        int result = cmuopEntityMapper.deleteByPrimaryKey(cmuopEntityTypeDTO.getEntityId());

        Example example = new Example(CmuopEntityType.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("entityCode", cmuopEntityTypeDTO.getEntityCode());
        result += cmuopEntityTypeMapper.deleteByExample(example);
        return result;
    }

    @Override
    public void initEntityTypeByEntityCode(String entityCode, boolean isZhaoWen) {
        if(StringUtils.isBlank(entityCode)) {
            return;
        }
        Example example = new Example(CmuopEntityType.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("entityCode", entityCode);
        cmuopEntityTypeMapper.deleteByExample(example);

        CmuopDictItemDTO cmuopDictItemDTO = new CmuopDictItemDTO();
        cmuopDictItemDTO.setDictCode(isZhaoWen == true ? "three_level" : "field_level");
        List<CmuopDictItem> cmuopDictItemList = cmuopDictItemMapper.queryByCondition(cmuopDictItemDTO);
        for(CmuopDictItem dictItem : cmuopDictItemList) {
            CmuopEntityType entityType = new CmuopEntityType();
            entityType.setEntityCode(entityCode);
            entityType.setColumnType(dictItem.getValue());
            entityType.setOrderNum((int)dictItem.getSort());
            cmuopEntityTypeMapper.insertSelective(entityType);
        }
    }

    @Override
    public List<CmuopEntityTypeVO> queryEntityTypeByCode(String entityCode) {
        CmuopDictItemDTO cmuopDictItemDTO = new CmuopDictItemDTO();
        cmuopDictItemDTO.setDictCode("field_level");
        List<CmuopDictItem> cmuopDictItemList = cmuopDictItemMapper.queryByCondition(cmuopDictItemDTO);
        Map<String, String> dictMap = cmuopDictItemList.stream().collect(Collectors.toMap(CmuopDictItem::getValue, CmuopDictItem::getText));
        return queryEntityTypeListByEntityCode(entityCode, dictMap);
    }

    @Override
    public List<CmuopEntityTypeWithPointInfoTypeVO> queryEntityTypeWithPointInfoTypeByCode(String entityCode) {
        CmuopDictItemDTO cmuopDictItemDTO = new CmuopDictItemDTO();
        cmuopDictItemDTO.setDictCode("field_level");
        List<CmuopDictItem> cmuopDictItemList = cmuopDictItemMapper.queryByCondition(cmuopDictItemDTO);
        Map<String, String> dictMap = cmuopDictItemList.stream().collect(Collectors.toMap(CmuopDictItem::getValue, CmuopDictItem::getText));
        List<CmuopEntityTypeVO> entityTypeVOList = queryEntityTypeListByEntityCode(entityCode, dictMap);
        if(CollectionUtils.isEmpty(entityTypeVOList)) {
            return new ArrayList<>();
        }
        List<CmuopEntityTypeWithPointInfoTypeVO> entityTypeWithPointInfoTypeVOList = new ArrayList<>();
        for(CmuopEntityTypeVO entityTypeVO : entityTypeVOList) {
            CmuopEntityTypeWithPointInfoTypeVO pointInfoTypeVO = new CmuopEntityTypeWithPointInfoTypeVO();
            BeanUtils.copyProperties(entityTypeVO, pointInfoTypeVO);
            if(COLUMN_TYPE_OF_POINT_INFO.equals(entityTypeVO.getColumnType())) {
                List<PointViewTypeVO> typeList = cmuopPointViewTypeService.getPointViewList();
                // PointViewTypeVO others = new PointViewTypeVO();
                // others.setInfoTypeName(POINT_INFO_NAME_FOR_OTHERS);
                // typeList.add(others);
                pointInfoTypeVO.setPointInfoTypeList(typeList);
            }
            entityTypeWithPointInfoTypeVOList.add(pointInfoTypeVO);
        }
        return entityTypeWithPointInfoTypeVOList;
    }

    private PageInfo<CmuopEntity> queryEntityList(String entityName, Integer pageNum, Integer pageSize) {
        Example example = new Example(CmuopEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ifCmuop", (byte)1);
        if(StringUtils.isNotBlank(entityName)){
            criteria.andLike("searchWords", "%" + entityName + "%");
        }
        example.orderBy("orderNum");
        PageHelper.startPage(pageNum, pageSize);
        List<CmuopEntity> cmuopEntityList = cmuopEntityMapper.selectByExample(example);
        PageInfo<CmuopEntity> pageInfo = new PageInfo<>(cmuopEntityList);
        return pageInfo;
    }

    private List<CmuopEntityTypeVO> queryEntityTypeListByEntityCode(String entityCode, Map<String, String> dictMap) {
        Example example = new Example(CmuopEntityType.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("entityCode", entityCode);
        example.orderBy("orderNum");
        List<CmuopEntityType> list = cmuopEntityTypeMapper.selectByExample(example);
        List<CmuopEntityTypeVO> voList = new ArrayList<>();
        for(CmuopEntityType type : list) {
            CmuopEntityTypeVO vo = new CmuopEntityTypeVO();
            BeanUtils.copyProperties(type, vo);
            vo.setColumnName(dictMap.get(type.getColumnType()));
            voList.add(vo);
        }
        return voList;
    }
}
