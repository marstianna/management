package com.dmall.managed.core.server.service.iface;

import com.dmall.managed.core.bean.HealthCheck;
import com.dmall.managed.core.bean.Node;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

/**
 * Created by zoupeng on 16/4/8.
 */
public interface ManagementServerService {
    /**
     * cluster节点朝中央节点注册
     * @param nodeInfo
     */
    void register(String nodeInfo);

    /**
     * 节点列表
     * @return  key:分组-value:同样类型的节点列表
     */
    Map<String, List<Node>> nodeList();

    /**
     * 节点登出,如果节点失效或者需要这个节点暂停服务,可以将这个节点踢离管理集群
     * @param nodeQualifier 节点标识
     */
    void logout(String nodeQualifier);

    /**
     * 执行任务
     * @param nodeQualifier 节点标识
     * @param operationQualifier    方法标识
     * @param params    方法入参
     * @return  执行结果
     */
    Object exec(String nodeQualifier,String operationQualifier,Map<String,Object> params);

    /**
     * 批量执行任务,不指定节点标识,将遍历所有在此注册的cluster
     * @param operationQualifier    方法表示
     * @param params    方法入参
     * @return  key:方法表示-value:执行结果
     */
    Map<String,Object> batchExec(String operationQualifier,Map<String,Object> params);

    /**
     * 手动注册节点信息
     * @param ip
     * @param port
     * @param path  路径(dubbo实现为服务接口)
     */
    void manualRegister(String ip,Integer port,String path);

    /**
     *  刷新当前所有存活节点信息
     */
    void refresh();

    /**
     * 向所有注册节点并且提供了healthCheck.operationQualifier方法的健康检查
     * @param healthCheck
     * @return  key=expectValue,Map(key=nodeQualifier,value=currentValue);
     */
    Pair<String, ? extends Map<String, String>> healthCheck(HealthCheck healthCheck);

    NodeService getNodeService();

    void setNodeService(NodeService nodeService);
}
