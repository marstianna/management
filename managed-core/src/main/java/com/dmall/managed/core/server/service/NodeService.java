package com.dmall.managed.core.server.service;

import com.dmall.managed.core.bean.HealthCheck;
import com.dmall.managed.core.bean.Node;
import com.dmall.managed.core.bean.Operation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

/**
 * Created by zoupeng on 16/3/10.
 */
public interface NodeService {
    /**
     * 主动从管理节点连接cluster节点,获取node信息后自动注册
     * @param ip
     * @param port
     * @param path
     */
    void register(String ip, Integer port, String path);

    /**
     * 根据node信息在管理节点注册
     *
     * @param node
     */
    void register(Node node);

    /**
     * 根据cluster的ip和port,解除掉这个节点的注册信息
     * @param ip
     * @param port
     */
    void deregister(String ip, Integer port);

    /**
     * 根据nodeQualifier解除掉这个节点的注册信息
     * @param nodeQualifier
     */
    void deregister(String nodeQualifier);

    /**
     * 获取节点信息
     *
     * @param nodeQualifier
     * @return
     */
    Node getNode(String nodeQualifier);

    /**
     * 获取所有节点
     *
     * @return key:分组名--value:节点列表
     */
    Map<String, List<Node>> getNodes();

    /**
     * 根据分组名获取节点
     *
     * @param group
     * @return
     */
    List<Node> getNodesByGroup(String group);

    /**
     * 向指定节点发送执行任务命令
     * @param operation
     * @param params
     * @return
     */
    Object exec(Operation operation, Map<String, Object> params);

    /**
     * 向指定节点发送执行任务命令
     * @param nodeQualifier
     * @param operationQualifier
     * @param params
     * @return
     */
    Object exec(String nodeQualifier, String operationQualifier, Map<String, Object> params);

    /**
     * 向所有向管理节点注册过的cluster发送operationQualifier执行任务
     * @param operationQualifier
     * @param params
     * @return
     */
    Map<String,Object> batchExec(String operationQualifier, Map<String, Object> params);

    /**
     * 向所有注册节点并且提供了healthCheck.operationQualifier方法的健康检查
     * @param healthCheck
     * @return  key=expectValue,Map(key=nodeQualifier,value=currentValue);
     */
    Pair<String,? extends Map<String,String>> healthCheck(HealthCheck healthCheck);
}
