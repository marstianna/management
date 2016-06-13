package com.dmall.managed.core.schedule;

import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.UnableToInterruptJobException;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by zoupeng on 6/7/16.
 */
public interface ScheduledService {
    /**
     * 添加一个job执行
     * @param target    job所属的Class
     * @param method    job执行的方法
     * @param args      方法的参数
     * @param group     job所属的group
     * @param name      job的名字
     * @param cron      cron表达式
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     * @throws SchedulerException
     */
    void addJob(Object target, Method method,Object[] args,String group,String name,String cron) throws NoSuchMethodException, ClassNotFoundException, SchedulerException;

    /**
     *  获取所有已添加的job
     * @return  List是Job属性集合
     * Map包含5个参数
     * cron-cron表达式
     * name-job的name
     * group-job所属的group
     * nextFireTime-下一次执行job的时间(unixTime)
     * previousFireTime-上一次执行job的时间(unixTime)
     * @throws SchedulerException
     */
     List<Map<String,Object>> getJobs() throws SchedulerException;

    /**
     * 立即出发一次job执行
     * @param jobKey
     * @throws SchedulerException
     */
     void triggerOnce(JobKey jobKey) throws SchedulerException;

    /**
     * 对于正在执行的job,将其暂停
     * @param jobKey
     * @throws SchedulerException
     */
     void pause(JobKey jobKey) throws SchedulerException;

    /**
     * 回复执行job
     * @param jobKey
     * @throws SchedulerException
     */
     void resume(JobKey jobKey) throws SchedulerException;

    /**
     * 修改job的cron表达式
     * @param jobKey
     * @param cron
     * @throws SchedulerException
     */
     void replaceTrigger(JobKey jobKey,String cron) throws SchedulerException;

    /**
     * 强制杀死阻塞的任务
     * @param jobKey
     * @throws UnableToInterruptJobException
     */
     void killRuningJob(JobKey jobKey) throws UnableToInterruptJobException;
}
