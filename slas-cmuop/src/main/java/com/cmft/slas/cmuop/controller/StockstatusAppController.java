package com.cmft.slas.cmuop.controller;

import com.cmft.slas.cmuop.dto.SortDTO;
import com.cmft.slas.cmuop.dto.StockstatusDTO;
import com.cmft.slas.cmuop.service.StockstatusService;
import com.cmft.slas.common.pojo.WebResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/app")
public class StockstatusAppController {

    @Autowired
    private StockstatusService stockstatusService;

    // 获取股票信息列表
    @ApiOperation(value = "根据查询规则查询涨跌幅列表初始化显示数据", notes = "查询股票信息列表", consumes = "application/json",
            produces = "application/json")
    @RequestMapping(value = "/stockstatusList", method = RequestMethod.GET)
    public WebResponse<List<StockstatusDTO>> queryStockstatusList(@ModelAttribute SortDTO sortDTO){
        WebResponse<List<StockstatusDTO>> webResponse = new WebResponse<>();
        List<StockstatusDTO> list = stockstatusService.getStockstatusList(sortDTO);
        webResponse.setBody(list);
        return webResponse;
    }
}
