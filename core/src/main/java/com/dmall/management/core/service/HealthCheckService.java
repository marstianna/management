package com.dmall.management.core.service;

import java.util.Map;

/**
 * Created by zoupeng on 16/3/4.
 */
public interface HealthCheckService {
    String healthCheck(Map<String,Object> params);
}
