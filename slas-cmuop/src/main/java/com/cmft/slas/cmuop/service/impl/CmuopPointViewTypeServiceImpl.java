package com.cmft.slas.cmuop.service.impl;

import com.cmft.slas.cmuop.common.constant.PointInfoConst;
import com.cmft.slas.cmuop.common.utils.BeanMapper;
import com.cmft.slas.cmuop.entity.CmuopDictItem;
import com.cmft.slas.cmuop.entity.PointInfo;
import com.cmft.slas.cmuop.entity.PointInfoType;
import com.cmft.slas.cmuop.mapper.CmuopDictItemMapper;
import com.cmft.slas.cmuop.mapper.CmuopPointInfoMapper;
import com.cmft.slas.cmuop.mapper.CmuopPointInfoTypeMapper;
import com.cmft.slas.cmuop.service.CmuopPointViewTypeService;
import com.cmft.slas.cmuop.vo.PointInfoVO;
import com.cmft.slas.cmuop.vo.PointViewTypeVO;
import com.cmft.slas.common.pojo.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CmuopPointViewTypeServiceImpl implements CmuopPointViewTypeService {

    @Autowired
    private BeanMapper beanMapper;

    @Autowired
    private CmuopPointInfoTypeMapper pointInfoTypeMapper;

    @Autowired
    private CmuopPointInfoMapper pointInfoMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public PageInfo<PointViewTypeVO> getPointViewPage(Boolean usedByApp, Page page) {
        Example example = new Example(PointInfoType.class);
        Example.Criteria criteria = example.createCriteria().andCondition("1=1").andEqualTo("isDelete", (byte)0);
        example.setOrderByClause("CONVERT(sort, UNSIGNED), update_time desc");
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<PointInfoType> infoList = pointInfoTypeMapper.selectByExample(example);
        PageInfo<PointInfoType> infoPage = new PageInfo<>(infoList);
        PageInfo<PointViewTypeVO> voPage = new PageInfo<>();
        BeanUtils.copyProperties(infoPage, voPage);
        List<PointViewTypeVO> voList = new ArrayList<>();
        for (PointInfoType info : infoList) {
            PointViewTypeVO vo = convertToVO(info);
            voList.add(vo);
        }
        voPage.setList(voList);
        return voPage;
    }

    @Override
    public List<PointViewTypeVO> getPointViewList() {
        Example example = new Example(PointInfoType.class);
        Example.Criteria criteria =
            example.createCriteria().andCondition("1=1").andEqualTo("isDelete", (byte)0).andEqualTo("ifShow", true);
        example.setOrderByClause("CONVERT(sort, UNSIGNED), update_time desc");
        List<PointInfoType> infoList = pointInfoTypeMapper.selectByExample(example);
        List<PointViewTypeVO> voList = new ArrayList<>();
        for (PointInfoType info : infoList) {
            PointViewTypeVO vo = convertToVO(info);
            voList.add(vo);
        }
        return voList;
    }

    private PointViewTypeVO convertToVO(PointInfoType info) {
        if(info == null) {
            return null;
        }
        PointViewTypeVO vo = new PointViewTypeVO();
        BeanUtils.copyProperties(info, vo);
        Example example = new Example(PointInfo.class);
        Example.Criteria criteria = example.createCriteria()
                .andEqualTo("infoType", info.getInfoType())
                .andEqualTo("isDelete",(byte)0);
        vo.setTotal(pointInfoMapper.selectCountByExample(example));
        return vo;
    }

    @Override
    public PointViewTypeVO getPointView(String infoType) {
        Example example = new Example(PointInfoType.class);
        Example.Criteria criteria = example.createCriteria()
                .andEqualTo("infoType", infoType)
                .andEqualTo("isDelete", (byte)0);
        List<PointInfoType> itemList = pointInfoTypeMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(itemList)) {
            log.error("type of point value with many item:{}",infoType);
            return new PointViewTypeVO();
        }
        return convertToVO(itemList.get(0));
    }

    @Override
    public int addPointView(PointViewTypeVO pointViewTypeVO) {
        if (pointViewTypeVO == null) {
            log.error("pointViewTypeVO is null:{}");
            return -1;
        }
        pointViewTypeVO.setInfoType(getInfoType());
        Example example = new Example(PointInfoType.class);
        Example.Criteria criteria = example.createCriteria()
                .andEqualTo("infoType", pointViewTypeVO.getInfoType());
        List<PointInfoType> itemList = pointInfoTypeMapper.selectByExample(example);
        if(!CollectionUtils.isEmpty(itemList)) {
            log.error("type of point value is exists:{}",pointViewTypeVO);
            return -2;
        }
        PointInfoType pointInfoType = new PointInfoType();
        BeanUtils.copyProperties(pointViewTypeVO,pointInfoType);
        return pointInfoTypeMapper.insertSelective(pointInfoType);
    }

    private String getInfoType() {
        Integer infoType = (Integer)redisTemplate.opsForValue().get(PointInfoConst.REDIS_KEY.getValue());
        if (infoType == null) {
            String maxType = pointInfoTypeMapper.getMaxInfoType();
            Integer initInfoType = Integer.valueOf(maxType) + 1;
            redisTemplate.opsForValue().set(PointInfoConst.REDIS_KEY.getValue(), initInfoType);
            return String.valueOf(initInfoType);
        }
        return String.valueOf(redisTemplate.opsForValue().increment(PointInfoConst.REDIS_KEY.getValue()));
        // String prevType = pointInfoTypeMapper.getMaxInfoType();
        // return String.valueOf(Integer.valueOf(prevType) + 1);
    }

    @Override
    public int updatePointView(PointViewTypeVO pointViewTypeVO) {
        if(pointViewTypeVO == null || StringUtils.isBlank(pointViewTypeVO.getInfoType())) {
            log.error("pointViewTypeVO or value is null:{}",pointViewTypeVO);
            return -1;
        }
        PointInfoType pointInfoType = new PointInfoType();
        BeanUtils.copyProperties(pointViewTypeVO,pointInfoType);
        Example example = new Example(PointInfoType.class);
        Example.Criteria criteria = example.createCriteria()
            .andEqualTo("infoType", pointViewTypeVO.getInfoType());
        return pointInfoTypeMapper.updateByExampleSelective(pointInfoType, example);
    }

    @Override
    public int deletePointView(String infoType) {
        if(StringUtils.isBlank(infoType)) {
            log.error("pointViewTypeVO or value is null:{}",infoType);
            return -1;
        }
        Example example = new Example(PointInfoType.class);
        Example.Criteria criteria = example.createCriteria()
                .andEqualTo("infoType", infoType);
        PointInfoType pointInfoType = new PointInfoType();
        pointInfoType.setIsDelete((byte)1);
        return pointInfoTypeMapper.updateByExampleSelective(pointInfoType, example);
        // return pointInfoTypeMapper.deleteByExample(example);
    }

    @Override
    public String updateIfShow(String infoType, Boolean ifShow) {
        Example example = new Example(PointInfoType.class);
        example.createCriteria().andEqualTo("infoType", infoType);
        int count = pointInfoTypeMapper.selectCountByExample(example);
        if (count < 1) {
            return "不存在该栏目";
        }
        PointViewTypeVO pointViewTypeVO = new PointViewTypeVO().setInfoType(infoType).setIfShow(ifShow);
        int updateCount = updatePointView(pointViewTypeVO);
        return updateCount > 0 ? "操作成功" : "操作失败";
    }
}
