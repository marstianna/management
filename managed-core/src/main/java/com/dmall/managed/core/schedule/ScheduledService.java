package com.dmall.managed.core.schedule;

import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.UnableToInterruptJobException;

import java.util.List;
import java.util.Map;

/**
 * Created by zoupeng on 6/7/16.
 */
public interface ScheduledService {
     List<Map<String,Object>> getJobs() throws SchedulerException;

     void triggerOnce(JobKey jobKey) throws SchedulerException;

     void pause(JobKey jobKey) throws SchedulerException;

     void resume(JobKey jobKey) throws SchedulerException;

     void replaceTrigger(JobKey jobKey,String cron) throws SchedulerException;

    /**
     * 强制杀死阻塞的任务
     * @param jobKey
     * @throws UnableToInterruptJobException
     */
     void killRuningJob(JobKey jobKey) throws UnableToInterruptJobException;
}
