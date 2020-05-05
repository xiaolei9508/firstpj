package com.cmft.slas.cmuop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmft.slas.cmuop.dto.CountDTO;
import com.cmft.slas.cmuop.mapper.ArticleKafkaMapper;
import com.cmft.slas.cmuop.mapper.UserMapper;
import com.cmft.slas.cmuop.mapper.UserViewMapper;
import com.cmft.slas.cmuop.service.StatisticService;
import com.cmft.slas.cmuop.vo.StatisticPerDayVO;

@Service
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    private ArticleKafkaMapper articleKafkaMapper;

    @Autowired
    private UserViewMapper userViewMapper;

    @Autowired
    private UserMapper userMapper;


    @Override
    public StatisticPerDayVO countPerDay(String day) {
        StatisticPerDayVO statisticPerDayVO = new StatisticPerDayVO();
        String from = day + " 00:00:00";
        String to = day + " 23:59:59";
        List<CountDTO> articleToday =
            articleKafkaMapper.countArticleKafka(from,to);
        int total = 0;
        for (CountDTO count : articleToday) {
            if (count.getCode() == 1) {
                total += count.getCount();
                statisticPerDayVO.setSynTodayFail(count.getCount());
            }
            if (count.getCode() == 0) {
                total += count.getCount();
                statisticPerDayVO.setSynTodaySuccess(count.getCount());
            }
        }
        statisticPerDayVO.setSynToday(total);
        List<CountDTO> articleTotal =
            articleKafkaMapper.countArticleKafka(null, to);
        total = 0;
        for (CountDTO count : articleToday) {
            if (count.getCode() == 1) {
                total += count.getCount();
                statisticPerDayVO.setTotalFail(count.getCount());
            }
            if (count.getCode() == 0) {
                total += count.getCount();
                statisticPerDayVO.setTotalSuccess(count.getCount());
            }
        }
        statisticPerDayVO.setTotal(total);
        
        statisticPerDayVO.setArticleViewToday(userViewMapper.countByDay(from, to));
        statisticPerDayVO.setArticleViewTotal(userViewMapper.countByDay(null, to));
        statisticPerDayVO.setUserToday(userMapper.countByDay(from, to));
        statisticPerDayVO.setUserTotal(userMapper.countByDay(null, to));

        return statisticPerDayVO;
    }

}
