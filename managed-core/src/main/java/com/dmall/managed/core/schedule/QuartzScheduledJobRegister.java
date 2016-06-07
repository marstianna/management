package com.dmall.managed.core.schedule;

import com.dmall.managed.core.schedule.annotation.ScheduledJob;
import org.apache.commons.lang3.BooleanUtils;
import org.quartz.*;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.util.StringValueResolver;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zoupeng on 6/7/16.
 */
public class QuartzScheduledJobRegister implements EmbeddedValueResolverAware, ApplicationContextAware,
        ApplicationListener<ContextRefreshedEvent> {

    public static final String SCHEDULE_CLUSTER_CONCURRENT = "cluster_current";

    /**
     * 请使用与ScheduledServiceImpl同一个Scheduler
     */
    private Scheduler scheduler;

    private StringValueResolver embeddedValueResolver;


    private ApplicationContext applicationContext;


    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.embeddedValueResolver = resolver;
    }


    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext() == this.applicationContext) {
            try {
                scheduler.clear();
            } catch (SchedulerException ex) {
                throw new IllegalStateException(ex);
            }

            Map<String, ScheduledAware> awares = event.getApplicationContext().getBeansOfType(ScheduledAware.class);

            for (ScheduledAware aware : awares.values()) {

                Object bean = getRealObject(aware);

                final Method[] declaredMethods = bean.getClass().getDeclaredMethods();

                for (final Method method : declaredMethods) {
                    if (method.isAnnotationPresent(ScheduledJob.class)) {
                        ScheduledJob job = method.getAnnotation(ScheduledJob.class);
                        try {
                            registerJob(bean, method, job);
                        } catch (Exception e) {
                            throw new RuntimeException(e.toString(), e);
                        }
                    }
                }
            }
        }
    }

    public void registerJob(Object bean, Method method, ScheduledJob annotation) throws Exception {
        Class<?> targetClass = bean.getClass();

        MethodInvokingJobDetailFactoryBean jobFactory = new MethodInvokingJobDetailFactoryBean();

        jobFactory.setTargetObject(bean);
        jobFactory.setTargetMethod(method.getName());
        jobFactory.setGroup(annotation.group().isEmpty() ? targetClass.getSimpleName() + "_" + UUID.randomUUID()
                : annotation.group());
        jobFactory.setName(annotation.name().isEmpty() ? targetClass.getSimpleName() + "_" + UUID.randomUUID()
                : annotation.name());

        jobFactory.setConcurrent(annotation.concurrent());

        jobFactory.afterPropertiesSet();

        JobDetail jobDetail = jobFactory.getObject();

        jobDetail.getJobDataMap().put(SCHEDULE_CLUSTER_CONCURRENT, annotation.clusterConcurrent());

        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger()
                .withIdentity(jobDetail.getKey().getName() + "_trigger", jobDetail.getKey().getGroup() + "_triggers")
                .startNow();

        String cronExpression = annotation.cron();
        long fixedRate = annotation.fixedRate();
        if (!BooleanUtils.xor(new boolean[]{!cronExpression.isEmpty(), fixedRate >= 0})) {
            throw new IllegalStateException("'cronExpression' or 'fixedRate' is exclusively required. Offending class "
                    + targetClass.getName());
        }

        if (!cronExpression.isEmpty()) {
            if (embeddedValueResolver != null) {
                cronExpression = embeddedValueResolver.resolveStringValue(cronExpression);
            }

            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression));

        } else if (fixedRate >= 0) {
            triggerBuilder.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(fixedRate)
                    .repeatForever());
        }

        scheduler.scheduleJob(jobDetail, triggerBuilder.build());

    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    private Object getRealObject(Object obj) {
        if (AopUtils.isJdkDynamicProxy(obj)) {
            try {
                return ((Advised) obj).getTargetSource().getTarget();
            } catch (Exception e) {
                throw new RuntimeException("获取初始化ScheduledAware子类实例失败,请立即检查");
            }
        }
        return obj;
    }
}
