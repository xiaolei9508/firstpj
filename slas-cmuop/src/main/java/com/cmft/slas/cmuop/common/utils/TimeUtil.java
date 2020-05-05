package com.cmft.slas.cmuop.common.utils;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间戳处理工具类
 * 
 * @author wuk001
 * @date 2020/01/20
 */
public class TimeUtil {

    /**
     * 返回传入日期零点与当前日期零点日期差
     * 
     * @param date
     * @return
     */
    public static Integer getTimeInterval(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long time1 = cal.getTimeInMillis();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    public static Integer getTimeIntervalInMill(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long time1 = cal.getTimeInMillis();
        long time2 = System.currentTimeMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 返回与当前日期零点相差days天的日期凌晨Date对象
     * @param days
     * @return
     */
    public static Date getDateIntervalDays(long days) {
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).plusDays(days);
        return Date.from(localDateTime.toInstant(ZoneOffset.of("+8")));
    }

    public static void main(String[] args) {
        getDateIntervalDays(-14L);
        System.out.println(getDateIntervalDays(-100).getTime() * -1);
        System.out.println(getDateIntervalDays(-14).getTime() * -1);
    }
}
