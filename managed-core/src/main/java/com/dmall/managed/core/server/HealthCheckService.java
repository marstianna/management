package com.dmall.managed.core.server;

import com.dmall.managed.core.bean.HealthCheck;
import org.quartz.SchedulerException;

/**
 * Created by zoupeng on 16/3/30.
 */
public interface HealthCheckService {
    /**
     * 构造一个HealthCheck,nodeQualifier可以为空
     * 该方法需要为HealthCheck添加defaultValue,AutoFix和WarningStrategy
     * @param nodeQualifier
     * @param operationQualifier
     * @return
     */
    HealthCheck build(String nodeQualifier,String operationQualifier);

    /**
     * 根据healthCheck的cron字段,定时执行这个healthcheck
     * @param healthCheck
     */
    void scheduledHealthCheck(HealthCheck healthCheck) throws NoSuchMethodException, ClassNotFoundException, SchedulerException;

    /**
     * 立刻执行healthCheck
     * @param healthCheck
     */
    void immediatelyHealthCheck(HealthCheck healthCheck);
}
