package com.cmft.slas.cmuop.common.service;

import java.util.List;

import org.quartz.SchedulerException;

import com.cmft.slas.cmuop.common.quartz.dto.QuartzTaskInfoDTO;


public interface CmuopQuartzService {

    public List<QuartzTaskInfoDTO> listJobs();

    public void addJob(QuartzTaskInfoDTO info);

    public void editJob(QuartzTaskInfoDTO info);

    public void deleteJob(String jobName, String jobGroup);

    public void pauseJob(String jobName, String jobGroup);

    public void resumeJob(String jobName, String jobGroup);

    public boolean checkJobExists(String jobName, String jobGroup) throws SchedulerException;
}
