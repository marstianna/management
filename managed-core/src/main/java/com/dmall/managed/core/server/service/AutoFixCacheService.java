package com.dmall.managed.core.server.service;

import com.dmall.managed.core.bean.HealthCheck;

/**
 * Created by zoupeng on 16/3/30.
 */
public interface AutoFixCacheService {
    void autoFix(HealthCheck healthCheck);
}
