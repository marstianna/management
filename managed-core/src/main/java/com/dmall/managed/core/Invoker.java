package com.dmall.managed.core;

import com.dmall.managed.core.bean.Operation;

import java.util.Map;

/**
 * 执行operation,
 * 如果是master,那么应该调用远程cluster服务,
 * 如果是cluster,则直接执行operation
 *
 * Created by zoupeng on 16/3/2.
 */
public interface Invoker {
    Object invoke(Operation operation, Map<String, Object> params);
}
