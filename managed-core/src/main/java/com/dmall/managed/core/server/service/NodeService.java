package com.dmall.managed.core.server.service;

import com.dmall.managed.core.bean.Node;
import com.dmall.managed.core.bean.Operation;

import java.util.List;
import java.util.Map;

/**
 * Created by zoupeng on 16/3/10.
 */
public interface NodeService {
    void register(String ip, Integer port, String path);

    void register(Node node);

    void deregister(String ip, Integer port);

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

    Object exec(Operation operation, Map<String, Object> params);

    Object exec(String nodeQualifier, String operationQualifier, Map<String, Object> params);

    List<Object> batchExec(String operationQualifier, Map<String, Object> params);
}
