package com.cmft.slas.cmuop.controller;

import java.util.List;

import com.cmft.slas.cmuop.dto.SortDTO;
import com.cmft.slas.common.pojo.Page;
import com.cmft.slas.common.pojo.WebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cmft.slas.cmuop.common.constant.Constant;
import com.cmft.slas.cmuop.dto.StockstatusDTO;
import com.cmft.slas.cmuop.service.StockstatusService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api("股票信息管理接口")
@RequestMapping(value = "v1/stockstatus")
public class StockstatusWebController {

    @Autowired
    private StockstatusService stockstatusService;

    // 添加股票信息
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加股票信息", notes = "添加股票信息", produces = "application/json")
    public WebResponse<String> addStockstatus(@RequestBody StockstatusDTO stockstatusDTO) {
        int res = stockstatusService.addStockstatus(stockstatusDTO);
        return res > 0 ? new WebResponse<>("成功") : new WebResponse<>(Constant.N, "新增失败");
    }

    // 删除股票信息
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除股票信息", notes = "删除股票信息", produces = "application/json")
    public WebResponse<String> deleteStockstatus(@PathVariable Long id) {
        int res = stockstatusService.deleteStockstatus(id);
        return res > 0 ? new WebResponse<>("删除成功") : new WebResponse<>(Constant.N, "删除失败");
    }

    // 更新股票信息
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ApiOperation(value = "修改股票信息", notes = "更新股票信息", produces = "application/json")
    public WebResponse<String> updateStockstatus(@RequestBody StockstatusDTO stockstatusDTO) {
        int res = stockstatusService.updateStockstatus(stockstatusDTO);
        return res > 0 ? new WebResponse<>("更新成功") : new WebResponse<>(Constant.N, "更新失败");
    }

    // 根据id查询股票信息
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "根据id股票信息信息", notes = "根据id股票信息信息", produces = "application/json")
    public WebResponse<StockstatusDTO> queryStockstatusById(@PathVariable Long id) {
        WebResponse<StockstatusDTO> webResponse = new WebResponse<>();
        StockstatusDTO stockstatusDTO = stockstatusService.queryById(id);
        if (stockstatusDTO == null) {
            webResponse.setCode(Constant.N);
        }
        webResponse.setBody(stockstatusDTO);
        return webResponse;
    }
      
    // 获取股票信息分页
    @ApiOperation(value = "股票专栏查询记录接口", notes = "股票赚了查询接口", consumes = "application/x-www-form-urlencoded",
        produces = "application/json")
    @RequestMapping(value = "/stockstatusPage", method = RequestMethod.POST)
    public WebResponse<PageInfo<StockstatusDTO>> queryResourcePage(@ModelAttribute StockstatusDTO stockstatusDTO,
        @ModelAttribute Page page) throws Exception {
        WebResponse<PageInfo<StockstatusDTO>> webResponse = new WebResponse<>();
        PageInfo<StockstatusDTO> packageList = stockstatusService.queryByPage(stockstatusDTO, page);
        webResponse.setBody(packageList);
        return webResponse;
    }
}
