package com.dmall.management.core;

import com.dmall.management.core.bean.Node;
import com.dmall.management.core.bean.Operation;
import com.dmall.management.core.bean.Service;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * master节点用于缓存cluster向自己注册的信息的管理
 *
 * Created by zoupeng on 16/3/3.
 */
public class NodeCache {
    private static Table<String,String,Operation> caches = HashBasedTable.create();

    /**
     * 注册一个cluster节点
     * @param node
     */
    public void register(Node node){
        for(Service service : node.getServices()){
            for(Operation operation : service.getOperations()){
                caches.put(node.getNodeQualifier(),operation.getQualifier(),operation);
            }
        }
    }

    /**
     * 移除一个cluster节点
     * @param node
     */
    public void deregister(Node node){
        String row = node.getNodeQualifier();
        for(Service service : node.getServices()){
            for(Operation operation : service.getOperations()){
                caches.remove(row,operation.getQualifier());
            }
        }

    }

    /**
     * 移除一个cluster节点
     * @param nodeQualifer
     */
    public void deregister(String nodeQualifer){
        Map<String,Operation> nodeDetail = caches.row(nodeQualifer);
        for(String operationQualifier : nodeDetail.keySet()){
            caches.remove(nodeQualifer,operationQualifier);
        }
    }

    public Operation getOperation(String nodeQualifier,String operationQualifier){
        return caches.get(nodeQualifier,operationQualifier);
    }

    public Set<Node> getAllNodes(){
        Set<Table.Cell<String, String, Operation>> set = caches.cellSet();
        Set<Node> result = new HashSet<Node>();
        for(Table.Cell<String, String, Operation> cell : set){
            result.add(cell.getValue().getService().getNode());
        }
        return result;
    }

    public Map<String,Operation> getOperationByQualified(String qualified){
        return caches.column(qualified);
    }
}
