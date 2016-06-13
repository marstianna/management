package com.dmall.managed.core.server;

import com.dmall.managed.core.bean.HealthCheck;

/**
 * 当healthCheck对象的AutoFixCacheService属性不为空,
 * 那么如果检测到expectValue != currentValue,就会自动调用
 * autoFix去修复
 * Created by zoupeng on 16/3/30.
 */
public interface AutoFixCacheService {
    void autoFix(HealthCheck healthCheck);
}
