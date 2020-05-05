package com.cmft.slas.cmuop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cmft.slas.cmuop.service.ColumnService;
import com.cmft.slas.cmuop.vo.ColumnVO;
import com.cmft.slas.common.pojo.WebResponse;

/**
 * 获取分栏列表
 * 
 * @author wuk001
 * @date 2020/12/30
 */
@RestController
@RequestMapping("/app")
public class ColumnAppController {

    @Autowired
    private ColumnService columnService;

    @GetMapping("/columns/{entityCode}")
    public WebResponse<List<ColumnVO>> getAppColumns(@PathVariable String entityCode) {
        return WebResponse.success(columnService.getAppColumns(entityCode));
    }
}
