package com.cmft.slas.cmuop.service;

import com.cmft.slas.cmuop.entity.PointInfoViewRecord;
import com.cmft.slas.cmuop.vo.PointInfoVO;
import com.cmft.slas.common.pojo.WebResponse;
import com.github.pagehelper.PageInfo;

public interface CmuopPointService {
    PageInfo<PointInfoVO> queryPointInfoPage(String infoType, String title, Boolean ifShow,
        Integer pageNum, Integer pageSize);

    WebResponse<PointInfoVO> queryPointInfoByInfoId(String infoId);

    WebResponse<String> addPointInfo(PointInfoVO pointInfoVO, Boolean haveOutline);

    WebResponse<String> updatePointInfo(PointInfoVO pointInfoVO, Boolean haveOutline);

    WebResponse<String> deletePointInfoByInfoId(String infoId);

    WebResponse<String> addPointInfoViewRecord(PointInfoViewRecord viewRecord) throws Exception;

    Boolean ifRead(String uid, Long infoId);

    WebResponse<String> updateMonthlyImgUrl(PointInfoVO pointInfoVO);
}
