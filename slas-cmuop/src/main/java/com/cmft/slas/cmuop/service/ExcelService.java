package com.cmft.slas.cmuop.service;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cmft.slas.cmuop.vo.ArticlePreview;

public interface ExcelService {

    InputStream writeExcel(List<? extends ArticlePreview> preview, Date dateFrom, Date dateTo,
        Map<String, String> entityMap);

}
