package com.cmft.slas.cmuop.service.impl;

import com.cmft.slas.cmuop.common.constant.PointInfoConst;
import com.cmft.slas.cmuop.common.utils.BeanMapper;
import com.cmft.slas.cmuop.entity.CmuopDictItem;
import com.cmft.slas.cmuop.entity.PointInfo;
import com.cmft.slas.cmuop.entity.PointInfoOutline;
import com.cmft.slas.cmuop.entity.PointInfoViewRecord;
import com.cmft.slas.cmuop.mapper.*;
import com.cmft.slas.cmuop.service.CmuopPointService;
import com.cmft.slas.cmuop.vo.PointInfoVO;
import com.cmft.slas.common.pojo.WebResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CmuopPointServiceImpl implements CmuopPointService {

    @Autowired
    private CmuopPointInfoMapper pointInfoMapper;

    @Autowired
    private CmuopPointInfoOutlineMapper outlineMapper;

    @Autowired
    private BeanMapper beanMapper;

    @Autowired
    private PointInfoViewRecordMapper viewRecordMapper;

    @Autowired
    private CmuopDictItemMapper dictItemMapper;

    @Autowired
    private CmuopPointInfoTypeMapper pointInfoTypeMapper;

    private static final String MONTHLY_IMG_URL_DICT_CODE = "img_url_for_monthly";

    @Override
    public PageInfo<PointInfoVO> queryPointInfoPage(String infoType, String title, Boolean ifShow,
        Integer pageNum, Integer pageSize) {
        pageNum = ObjectUtils.firstNonNull(pageNum, 1);
        pageSize = ObjectUtils.firstNonNull(pageSize, 10);
        Example example = new Example(PointInfo.class);
        Example.Criteria criteria = example.createCriteria()
                .andEqualTo("infoType", infoType)
                .andEqualTo("isDelete",(byte)0);
        if(StringUtils.isNotBlank(title)) {
            criteria.andLike("title", "%"+title+"%");
        }
        if(ifShow != null) {
            criteria.andEqualTo("ifShow", ifShow);
        }
        example.orderBy("orderNum").orderBy("updateTime").desc();

        PageHelper.startPage(pageNum, pageSize);
        List<PointInfo> pointInfoList = pointInfoMapper.selectByExample(example);
        PageInfo<PointInfo> infoPageInfo = new PageInfo<>(pointInfoList);
        PageInfo<PointInfoVO> voPageInfo = new PageInfo<PointInfoVO>();
        BeanUtils.copyProperties(infoPageInfo, voPageInfo);
        if(voPageInfo != null && !CollectionUtils.isEmpty(voPageInfo.getList())) {
            List<PointInfoVO> voList = new ArrayList();
            for(PointInfo info : infoPageInfo.getList()) {
                PointInfoVO vo = new PointInfoVO();
                BeanUtils.copyProperties(info, vo);
                if (haveOutline(infoType)) {
                    vo.setOutlineList(queryPointInfoOutlineByInfoId(vo.getInfoId()));
                }
                voList.add(vo);
            }
            voPageInfo.setList(voList);
        }
        appendBkImg(voPageInfo.getList(), infoType);
        return voPageInfo;
    }

    private void appendBkImg(List<PointInfoVO> list, String infoType) {
        Example example = new Example(com.cmft.slas.cmuop.entity.PointInfoType.class);
        example.createCriteria().andEqualTo("infoType", infoType);
        List<com.cmft.slas.cmuop.entity.PointInfoType> types = pointInfoTypeMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(types))
            return;
        String img = types.get(0).getImgUrl();
        list.forEach(vo -> vo.setImgUrl(img));
    }

    private Boolean haveOutline(String infoType) {
        Example example = new Example(com.cmft.slas.cmuop.entity.PointInfoType.class);
        example.createCriteria().andEqualTo("infoType", infoType);
        List<com.cmft.slas.cmuop.entity.PointInfoType> types = pointInfoTypeMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(types))
            return false;
        return types.get(0).getHaveOutline();
    }

    @Override
    public WebResponse<PointInfoVO> queryPointInfoByInfoId(String infoId) {
        if(StringUtils.isBlank(infoId)) {
            return WebResponse.error("infoId is null", null);
        }
        Example example = new Example(PointInfo.class);
        Example.Criteria criteria = example.createCriteria()
                .andEqualTo("infoId", infoId)
                .andEqualTo("isDelete",(byte)0);
        List<PointInfo> pointInfoList = pointInfoMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(pointInfoList)) {
            return WebResponse.success(null);
        }
        PointInfo pointInfo = pointInfoList.get(0);
        PointInfoVO pointInfoVO = new PointInfoVO();
        BeanUtils.copyProperties(pointInfo, pointInfoVO);
        if (haveOutline(pointInfo.getInfoType())) {
            pointInfoVO.setOutlineList(queryPointInfoOutlineByInfoId(pointInfo.getInfoId()));
        }
        return WebResponse.success(pointInfoVO);
    }

    @Override
    @Transactional
    public WebResponse<String> addPointInfo(PointInfoVO pointInfoVO, Boolean haveOutline) {
        PointInfo pointInfo = new PointInfo();
        BeanUtils.copyProperties(pointInfoVO, pointInfo);
        pointInfo.setInfoId(null);
        int res = pointInfoMapper.insertSelective(pointInfo);
        if (haveOutline && !CollectionUtils.isEmpty(pointInfoVO.getOutlineList())) {
            int index = 1;
            for(String text : pointInfoVO.getOutlineList()) {
                if(StringUtils.isBlank(text)) {
                    continue;
                }
                PointInfoOutline outline = new PointInfoOutline();
                outline.setOutlineId(null);
                outline.setInfoId(pointInfo.getInfoId());
                outline.setInfoType(pointInfo.getInfoType());
                outline.setOutline(text);
                outline.setOrderNum(index++);
                outlineMapper.insertSelective(outline);
            }
        }
        return res == 1 ? WebResponse.success(String.valueOf(pointInfo.getInfoId()), "新增成功") : WebResponse.error("新增失败",null);
    }

    @Override
    @Transactional
    public WebResponse<String> updatePointInfo(PointInfoVO vo, Boolean haveOutline) {
        if(vo == null || vo.getInfoId() == null) {
            return WebResponse.error("param is invalid", null);
        }
        PointInfo info = new PointInfo();
        BeanUtils.copyProperties(vo, info);
        info.setInfoId(vo.getInfoId());
        int res = pointInfoMapper.updateByPrimaryKeySelective(info);
        if (haveOutline) {
            Example example = new Example(PointInfoOutline.class);
            Example.Criteria criteria = example.createCriteria().andEqualTo("infoId", vo.getInfoId());
            outlineMapper.deleteByExample(example);
            if (!CollectionUtils.isEmpty(vo.getOutlineList())) {
                int index = 1;
                for (String text : vo.getOutlineList()) {
                    if (StringUtils.isBlank(text)) {
                        continue;
                    }
                    PointInfoOutline outline = new PointInfoOutline();
                    outline.setOutlineId(null);
                    outline.setInfoId(vo.getInfoId());
                    outline.setInfoType(vo.getInfoType());
                    outline.setOutline(text);
                    outline.setOrderNum(index++);
                    outlineMapper.insertSelective(outline);
                }
            }
        }
        return res == 1 ? WebResponse.success(String.valueOf(vo.getInfoId()), "更新成功") : WebResponse.error("更新失败",null);
    }

    @Override
    public WebResponse<String> deletePointInfoByInfoId(String infoId) {
        if(StringUtils.isBlank(infoId)) {
            return WebResponse.error("infoId is null", null);
        }

        Example example1 = new Example(PointInfo.class);
        Example.Criteria criteria1 = example1.createCriteria().andEqualTo("infoId", infoId);
        int res = outlineMapper.deleteByExample(example1);

        Example example2 = new Example(PointInfoOutline.class);
        Example.Criteria criteria2 = example2.createCriteria().andEqualTo("infoId", infoId);
        pointInfoMapper.deleteByExample(example2);
        return res == 1 ? WebResponse.success(String.valueOf(infoId), "删除成功") : WebResponse.error("删除失败",null);

    }


    private List<String> queryPointInfoOutlineByInfoId(Long infoId) {
        Example example = new Example(PointInfoOutline.class);
        Example.Criteria criteria = example.createCriteria()
                .andEqualTo("infoId", infoId)
                .andEqualTo("isDelete", (byte)0);
        example.orderBy("orderNum").orderBy("updateTime");
        List<PointInfoOutline> outlineList = outlineMapper.selectByExample(example);
        return outlineList.stream().map(PointInfoOutline::getOutline).collect(Collectors.toList());
    }


    @Override
    @Transactional
    public WebResponse<String> addPointInfoViewRecord(PointInfoViewRecord viewRecord) throws Exception {
        if(viewRecord == null || viewRecord.getInfoId() == null || StringUtils.isBlank(viewRecord.getUid())) {
            return WebResponse.error("view record is invalid", viewRecord.toString());
        }
        viewRecord.setViewRecordId(null);
        int res = viewRecordMapper.insertSelective(viewRecord);
        if(res == 1) {
            for(int i = 0; i < 3; i++) {
                Example example = new Example(PointInfo.class);
                Example.Criteria criteria = example.createCriteria()
                        .andEqualTo("infoId", viewRecord.getInfoId())
                        .andEqualTo("isDelete",(byte)0);
                List<PointInfo> infoList = pointInfoMapper.selectByExample(example);
                if(CollectionUtils.isEmpty(infoList)) {
                    log.error("point info is null:{}", viewRecord.getInfoId());
                    throw new RuntimeException("point info is null");
                }
                int viewCount = infoList.get(0).getViewCount();
                PointInfo info = new PointInfo();
                info.setViewCount(viewCount+1);
                info.setUpdateTime(infoList.get(0).getUpdateTime());
                criteria.andEqualTo("viewCount", viewCount);
                res = pointInfoMapper.updateByExampleSelective(info, example);
                if(res == 1) {
                    return WebResponse.success("新增浏览记录成功");
                }
            }
        }
        throw new RuntimeException("新增浏览记录失败");
    }

    @Override
    public Boolean ifRead(String uid, Long infoId) {
        Example example = new Example(PointInfoViewRecord.class);
        Example.Criteria criteria = example.createCriteria()
                .andEqualTo("uid", uid)
                .andEqualTo("infoId", infoId)
                .andEqualTo("isDelete", (byte)0);
        return viewRecordMapper.selectCountByExample(example) > 0;
    }

    @Override
    public WebResponse<String> updateMonthlyImgUrl(PointInfoVO vo) {
        if (vo == null || !PointInfoConst.MONTHLY.getValue().equals(vo.getInfoType())
            || StringUtils.isBlank(vo.getImgUrl())) {
            return WebResponse.error("param is invalid", null);
        }
        log.info("统一更新月报缩略图:{}", vo.toString());
        CmuopDictItem dictItem = new CmuopDictItem();
        dictItem.setText(vo.getImgUrl());
        Example example1 = new Example(CmuopDictItem.class);
        Example.Criteria criteria1 = example1.createCriteria().andEqualTo("dictCode", MONTHLY_IMG_URL_DICT_CODE);
        int res = dictItemMapper.updateByExampleSelective(dictItem, example1);

        PointInfo pointInfo = new PointInfo();
        pointInfo.setImgUrl(vo.getImgUrl());
        Example example2 = new Example(PointInfo.class);
        Example.Criteria criteria2 = example2.createCriteria()
            .andEqualTo("infoType", PointInfoConst.MONTHLY.getValue())
                .andEqualTo("isDelete",(byte)0);
        pointInfoMapper.updateByExampleSelective(pointInfo,example2);

        return res == 1 ? WebResponse.success(String.valueOf(vo.getInfoId()), "更新成功") : WebResponse.error("更新失败",null);
    }
}
