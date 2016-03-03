package com.dmall.management.core;

import com.dmall.management.core.bean.Node;
import com.dmall.management.core.bean.Operation;
import com.dmall.management.core.bean.Service;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * Created by zoupeng on 16/3/3.
 */
public class NodeCache {
    private Table<String,String,Operation> caches = HashBasedTable.create();

    public void register(Node node){
        String row = rowBuilder(node);
        for(Service service : node.getServices()){
            for(Operation operation : service.getOperations()){
                caches.put(row,operation.getQualifier(),operation);
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

    private String rowBuilder(Node node){
        return node.getIp()+":"+node.getPort()+"/"+node.getDisplayName();
    }
}
