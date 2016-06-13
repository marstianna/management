package com.dmall.managed.core.server;

import com.dmall.managed.core.bean.HealthCheck;

/**
 * healthCheck的报警策略,如果出现currentValue和DefaultValue不相等的情况
 * 且isWarning == true,那么将会执行warning;
 * Created by zoupeng on 16/3/29.
 */
public interface WarningStrategy {
    void warning(HealthCheck healthCheck);
}
