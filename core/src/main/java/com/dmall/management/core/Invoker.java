package com.dmall.management.core;

import java.util.Map;

/**
 * 执行operation,
 * 如果是master,那么应该调用远程cluster服务,
 * 如果是cluster,则直接执行operation
 *
 * Created by zoupeng on 16/3/2.
 */
public interface Invoker {
    Object invoke(String nodeQualifier,String operationQualifier,Map<String,Object> params);
}
