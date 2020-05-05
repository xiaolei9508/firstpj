package com.cmft.slas.cmuop.service;

import com.cmft.slas.cmuop.vo.StatisticPerDayVO;

public interface StatisticService {
    StatisticPerDayVO countPerDay(String day);
}
