package com.cmft.slas.cmuop.service;

import java.util.List;

import com.cmft.slas.cmuop.vo.ColumnVO;

public interface ColumnService {

    List<ColumnVO> getAppColumns(String entityCode);
}
