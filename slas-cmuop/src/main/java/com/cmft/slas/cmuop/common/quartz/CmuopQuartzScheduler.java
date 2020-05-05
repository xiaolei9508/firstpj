package com.cmft.slas.cmuop.common.quartz;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import com.cmft.slas.cmuop.common.quartz.dto.QuartzTaskInfoDTO;
import com.cmft.slas.cmuop.common.service.CmuopQuartzService;

/**
 * 任务配置
 *
 * @author xiaojp001
 * @date 2018/05/08
 */
@Component
public class CmuopQuartzScheduler {
    @Autowired
    @Qualifier("cmuopSchedulerFactoryBean")
    private SchedulerFactoryBean cmuopSchedulerFactoryBean;

    @Autowired
    private CmuopQuartzService quartzService;

    public void scheduleJobs() throws SchedulerException {
        Scheduler scheduler = cmuopSchedulerFactoryBean.getScheduler();
        startCountArticleLikeJob(scheduler);
        startSynOaAuthority(scheduler);
    }

    private void startCountArticleLikeJob(Scheduler scheduler) throws SchedulerException {
        QuartzTaskInfoDTO info = new QuartzTaskInfoDTO();
        info.setJobName("CountArticleLikeJob");
        info.setCronExpression("0/15 * * * * ?");// 0 0/30 * * * ?
        info.setJobClass("com.cmft.slas.cmuop.quartz.job.CountArticleLikeJob");
        info.setJobDescription("统计用户行为数量");
        if (!quartzService.checkJobExists(info.getJobName(), info.getJobGroup())) {
            quartzService.addJob(info);
        } else {
            quartzService.editJob(info);
        }
    }

    private void startSynOaAuthority(Scheduler scheduler) throws SchedulerException {
        QuartzTaskInfoDTO info = new QuartzTaskInfoDTO();
        info.setJobName("SynOaAuthorityJob");
        info.setCronExpression("0 30 5 * * ?");// 0 30 5 * * ?
        info.setJobClass("com.cmft.slas.cmuop.quartz.job.SynOaAuthorityJob");
        info.setJobDescription("同步oa数据");
        if (!quartzService.checkJobExists(info.getJobName(), info.getJobGroup())) {
            quartzService.addJob(info);
        }
    }

}
