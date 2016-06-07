package com.dmall.managed.core.server.impl;

import com.dmall.managed.core.bean.Node;
import com.dmall.managed.core.bean.Operation;
import com.dmall.managed.core.server.RegisterStore;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 内存版的注册信息管理
 *
 * Created by zoupeng on 16/3/3.
 */
public class MemoryRegisterStore implements RegisterStore {
    private Map<String,Node> registerNodes = new HashMap<>();

    @Override
    public void store(Node node) {
        registerNodes.put(node.getNodeQualifier(),node);
    }

    @Override
    public Node delete(String nodeQualifier) {
        return registerNodes.remove(nodeQualifier);
    }

    @Override
    public void clear(){
        registerNodes.clear();
    }

    @Override
    public Node getNode(String nodeQualifier) {
        return registerNodes.get(nodeQualifier);
    }

    @Override
    public Map<String, List<Node>> getNodesGroupByGroupName() {
        Map<String,List<Node>> results = new HashMap<>();
        for(Node node : registerNodes.values()){
            if(results.containsKey(node.getGroup())){
                results.get(node.getGroup()).add(node);
            }else{
                results.put(node.getGroup(), Lists.newArrayList(node));
            }
        }
        return results;
    }

    @Override
    public List<Node> getNodes() {
        return Lists.newArrayList(registerNodes.values());
    }

    @Override
    public List<Node> getNodesByGroup(String group) {
        return getNodesGroupByGroupName().get(group);
    }

    @Override
    public List<Operation> getOperationsByNode(String nodeQualifier) {
        return getNode(nodeQualifier).allOperationsInNode();
    }

    @Override
    public Operation getOperation(String nodeQualifier, String operationQualifier) {
        Operation operation = null;
        for(Operation oper : getOperationsByNode(nodeQualifier)){
            if(oper.getQualifier().equals(operationQualifier)){
                operation = oper;
            }
        }
        return operation;
    }

    @Override
    public List<Operation> getOperations(String operationQualifier) {
        List<Operation> operations = new ArrayList<>();

        for(Node node : registerNodes.values()){
            Operation operation = getOperation(node.getNodeQualifier(),operationQualifier);
            if(operation != null){
                operations.add(operation);
            }
        }

        return operations;
    }
}
