package com.cmft.slas.cmuop.service;

import com.cmft.slas.cmuop.vo.LeaderPreview;
import com.cmft.slas.common.pojo.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CmuopWebUserService {

    List<LeaderPreview> getLeaderList(String entityCode);

    String updateLeaderOrder(String uid, String entityCode, Integer orderNum);

    String updateEntity(String entityCode);

}
