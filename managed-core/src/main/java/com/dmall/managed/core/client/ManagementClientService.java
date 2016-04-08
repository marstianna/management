package com.dmall.managed.core.client;

import com.dmall.managed.core.Invoker;
import com.dmall.managed.core.bean.Node;

import java.util.Map;

/**
 * Created by zoupeng on 16/4/7.
 */
public interface ManagementClientService {
    /**
     * cluster将反射执行任务
     * @param operationQualifier
     * @param params
     * @return
     */
    Object exec(String operationQualifier,Map<String,Object> params);

    /**
     * 获取当前节点的信息
     * @return
     */
    Node registerNodeInfo();

    Invoker getInvoker() ;

    void setInvoker(Invoker invoker);

    NodeClient getNodeClient();

    void setNodeClient(NodeClient nodeClient);
}
