package com.dmall.management.core;

import com.dmall.management.core.bean.Node;
import com.dmall.management.core.bean.Operation;
import com.dmall.management.core.bean.Service;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.Map;

/**
 * Created by zoupeng on 16/3/3.
 */
public class NodeCache {
    private static Table<String,String,Operation> caches = HashBasedTable.create();

    public void register(Node node){
        for(Service service : node.getServices()){
            for(Operation operation : service.getOperations()){
                caches.put(node.getNodeQualifier(),operation.getQualifier(),operation);
            }
        }
    }

    public void deregister(Node node){
        String row = rowBuilder(node);
        for(Service service : node.getServices()){
            for(Operation operation : service.getOperations()){
                caches.remove(row,operation.getQualifier());
            }
        }

    }

    public void deregister(String nodeQualifer){
        Map<String,Operation> nodeDetail = caches.row(nodeQualifer);
        for(String operationQualifier : nodeDetail.keySet()){
            caches.remove(nodeQualifer,operationQualifier);
        }
    }

    public Map<String,Operation> getOperationByQualified(String qualified){
        return caches.column(qualified);
    }

    private String rowBuilder(Node node){
        return node.getIp()+":"+node.getPort()+"/"+node.getDisplayName();
    }
}
