package com.dmall.managed.core.server.impl;

import com.dmall.managed.core.bean.HealthCheck;
import com.dmall.managed.core.schedule.ScheduledService;
import com.dmall.managed.core.server.HealthCheckService;
import com.dmall.managed.core.server.NodeService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by zoupeng on 6/13/16.
 */
public abstract class AbstractHealthCheckService implements HealthCheckService {
    private ScheduledService scheduledService;
    private NodeService nodeService;
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractHealthCheckService.class);

    @Override
    public void scheduledHealthCheck(HealthCheck healthCheck) {
        if(nodeService == null || StringUtils.isBlank(healthCheck.getCron())){
            return;
        }
        try {
            Class<?> targetClass = nodeService.getClass();
            Method method = targetClass.getMethod("healthCheck", HealthCheck.class);
            scheduledService.addJob(targetClass, method, new Object[]{healthCheck}, healthCheck.getTargetOperationQualifier(), healthCheck.getTargetOperationQualifier(), healthCheck.getCron());
        }catch (Exception e){
            LOGGER.warn("添加job失败,请检查传入参数是否正确",e);
        }
    }

    @Override
    public void immediatelyHealthCheck(HealthCheck healthCheck) {
        nodeService.healthCheck(healthCheck);
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public void setScheduledService(ScheduledService scheduledService) {
        this.scheduledService = scheduledService;
    }
}
