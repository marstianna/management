package com.dmall.managed.core.schedule.impl;

import com.dmall.managed.core.schedule.QuartzScheduledJobRegister;
import com.dmall.managed.core.schedule.ScheduledService;
import com.dmall.managed.core.util.DateTool;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zoupeng on 6/7/16.
 */
public class ScheduledServiceImpl implements ScheduledService {
    /**
     * 请使用与QuartzScheduledJobRegister同一个Scheduler
     */
    private Scheduler quartzScheduler;

    public static final String SCHEDULE_TRIGGER_MANUALLY = "manually_invoke";

    @Override
    public void addJob(Object target, Method method,Object[] args, String group, String name, String cron) throws NoSuchMethodException, ClassNotFoundException, SchedulerException {
        MethodInvokingJobDetailFactoryBean jobFactory = new MethodInvokingJobDetailFactoryBean();

        jobFactory.setTargetObject(target);
        jobFactory.setTargetMethod(method.getName());
        jobFactory.setGroup(group);
        jobFactory.setName(name);
        jobFactory.setArguments(args);

        jobFactory.setConcurrent(true);

        jobFactory.afterPropertiesSet();

        JobDetail jobDetail = jobFactory.getObject();

        jobDetail.getJobDataMap().put(QuartzScheduledJobRegister.SCHEDULE_CLUSTER_CONCURRENT, false);

        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger()
                .withIdentity(jobDetail.getKey().getName() + "_trigger", jobDetail.getKey().getGroup() + "_triggers")
                .startNow();

        triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));

        quartzScheduler.scheduleJob(jobDetail, triggerBuilder.build());
    }

    @Override
    public List<Map<String,Object>> getJobs() throws SchedulerException {
        List<JobKey> runningJobs = Lists.transform(quartzScheduler.getCurrentlyExecutingJobs(), new Function<JobExecutionContext, JobKey>() {
            @Override
            public JobKey apply(JobExecutionContext input) {
                return input.getJobDetail().getKey();
            }
        });

        // retrieve all registered jobs
        List<Map<String,Object>> result = new ArrayList<>();
        Map<String,Object> entry = null;
        for (String groupName : quartzScheduler.getJobGroupNames()) {
            for (JobKey jobKey : quartzScheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                String jobName = jobKey.getName();
                String jobGroup = jobKey.getGroup();
                List<Trigger> triggers = (List<Trigger>) quartzScheduler.getTriggersOfJob(jobKey);
                Trigger trigger = triggers.get(0);// by now, for management UI, one trigger supported only

                entry = new HashMap<String, Object>();
                if(trigger instanceof CronTrigger){
                    entry.put("cron", ((CronTrigger) trigger).getCronExpression());
                }
                entry.put("name", jobName);
                entry.put("group", jobGroup);
                entry.put("nextFireTime", DateTool.toUnixTime(trigger.getNextFireTime()));
                entry.put("previousFireTime", DateTool.toUnixTime(trigger.getPreviousFireTime()));

                Trigger.TriggerState triggerState = quartzScheduler.getTriggerState(trigger.getKey());
                int status = Trigger.TriggerState.NORMAL == triggerState ? 1 : 0;
                status = runningJobs.contains(jobKey) ? 2 : status;
                entry.put("status", status);

                result.add(entry);
            }
        }
        return result;
    }

    @Override
    public void triggerOnce(JobKey jobKey) throws SchedulerException {
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(SCHEDULE_TRIGGER_MANUALLY, true);
        quartzScheduler.triggerJob(jobKey, dataMap);
    }

    @Override
    public void pause(JobKey jobKey) throws SchedulerException {
        quartzScheduler.pauseJob(jobKey);
    }

    @Override
    public void resume(JobKey jobKey) throws SchedulerException {
        quartzScheduler.resumeJob(jobKey);
    }

    @Override
    public void replaceTrigger(JobKey jobKey, String cron) throws SchedulerException {
        Preconditions.checkArgument(CronExpression.isValidExpression(cron),"非法的Cron表达式");

        JobDetail jobDetail = quartzScheduler.getJobDetail(jobKey);
        List<TriggerKey> jobkeys = Lists.transform(quartzScheduler.getTriggersOfJob(jobKey), new Function<Trigger, TriggerKey>() {
            @Override
            public TriggerKey apply(Trigger input) {
                Trigger trigger = (Trigger)input;
                return trigger.getKey();
            }
        });

        // Unschedule original triggers
        quartzScheduler.unscheduleJobs(jobkeys);
        // built new CRON trigger and reschedule

        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger().forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName() + "_trigger", jobDetail.getKey().getGroup() + "_triggers")
                .startNow();
        triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
        Trigger trigger = triggerBuilder.build();

        quartzScheduler.scheduleJob(trigger);
    }

    @Override
    public void killRuningJob(JobKey jobKey) throws UnableToInterruptJobException {
        quartzScheduler.interrupt(jobKey);
    }

    public Scheduler getQuartzScheduler() {
        return quartzScheduler;
    }

    public void setQuartzScheduler(Scheduler quartzScheduler) {
        this.quartzScheduler = quartzScheduler;
    }
}
