package com.dmall.managed.core.server;

import com.dmall.managed.core.bean.HealthCheck;

/**
 * Created by zoupeng on 16/3/30.
 */
public interface HealthCheckService {
    HealthCheck build(String nodeQualifier,String operationQualifier);

    void scheduledHealthCheck(HealthCheck healthCheck);

    void immediatelyHealthCheck(HealthCheck healthCheck);
}
