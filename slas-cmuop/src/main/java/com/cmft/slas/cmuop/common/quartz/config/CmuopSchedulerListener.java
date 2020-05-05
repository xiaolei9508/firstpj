package com.cmft.slas.cmuop.common.quartz.config;

import com.cmft.slas.cmuop.common.quartz.CmuopQuartzScheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;



/**
 * 注册[spring-boot]启动完成事件监听，用于启动job任务
 *
 * @author xiaojp001
 * @date 2018/05/08
 */
@Configuration
public class CmuopSchedulerListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    public CmuopQuartzScheduler quartzScheduler;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            quartzScheduler.scheduleJobs();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
