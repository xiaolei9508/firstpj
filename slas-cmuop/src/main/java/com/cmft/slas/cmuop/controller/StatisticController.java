package com.cmft.slas.cmuop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cmft.slas.cmuop.service.StatisticService;
import com.cmft.slas.cmuop.service.UserViewService;
import com.cmft.slas.cmuop.vo.StatisticPerDayVO;
import com.cmft.slas.cmuop.vo.UserViewVO;
import com.cmft.slas.common.pojo.WebResponse;

@RestController
@RequestMapping("/statistic")
public class StatisticController {

    @Autowired
    private StatisticService statisticService;

    @Autowired
    private UserViewService userViewService;

    @GetMapping("/day")
    public WebResponse<StatisticPerDayVO> getDayStatistic(@RequestParam String day) {
        return WebResponse.<StatisticPerDayVO>success(statisticService.countPerDay(day));
    }

    @GetMapping("/user")
    public WebResponse<List<UserViewVO>> getUserView(@RequestParam String uid, @RequestParam String day) {
        return WebResponse.<List<UserViewVO>>success(userViewService.getUserView(uid, day));
    }
}
