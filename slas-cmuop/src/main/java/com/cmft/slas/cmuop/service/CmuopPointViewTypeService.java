package com.cmft.slas.cmuop.service;

import com.cmft.slas.cmuop.vo.PointViewTypeVO;
import com.cmft.slas.common.pojo.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;


public interface CmuopPointViewTypeService {
    PageInfo<PointViewTypeVO> getPointViewPage(Boolean usedByApp, Page page);

    List<PointViewTypeVO> getPointViewList();

    PointViewTypeVO getPointView(String infoType);

    int addPointView(PointViewTypeVO pointViewTypeVO);

    int updatePointView(PointViewTypeVO pointViewTypeVO);

    int deletePointView(String infoType);

    String updateIfShow(String infoType, Boolean ifShow);
}
