package com.dmall.managed.core.server;

import com.dmall.managed.core.bean.Node;
import com.dmall.managed.core.bean.Operation;

import java.util.List;
import java.util.Map;

/**
 * 集群节点保存服务
 * 提供对集群节点注册后的记录和查询服务
 * 框架只实现了基于内存的管理版本,可以自己扩展基于redis或者数据库的版本
 * Created by zoupeng on 16/3/10.
 */
public interface RegisterStore {
    /**
     * 保存节点注册信息
     * @param node
     */
    void store(Node node);

    /**
     * 删除节点
     * @param nodeQualifier
     * @return
     */
    Node delete(String nodeQualifier);

    void clear();

    /**
     * 获取节点信息
     * @param nodeQualifier
     * @return
     */
    Node getNode(String nodeQualifier);

    /**
     * 获取所有节点
     * @return  key:分组名--value:节点列表
     */
    Map<String,List<Node>> getNodesGroupByGroupName();

    /**
     * 
     * @return
     */
    List<Node> getNodes();

    /**
     * 根据分组名获取节点
     * @param group
     * @return
     */
    List<Node> getNodesByGroup(String group);

    /**
     * 根据节点表示获取该节点所有服务
     * @param nodeQualifier
     * @return
     */
    List<Operation> getOperationsByNode(String nodeQualifier);

    /**
     *
     * @param nodeQualifier
     * @param operationQualifier
     * @return
     */
    Operation getOperation(String nodeQualifier, String operationQualifier);

    /**
     *
     * @param operationQualifier
     * @return
     */
    List<Operation> getOperations(String operationQualifier);
}
