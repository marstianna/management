package com.dmall.management.core;

import com.dmall.management.core.bean.Node;
import com.dmall.management.core.bean.Operation;
import com.dmall.management.core.bean.Parameter;
import com.dmall.management.core.bean.Service;

/**
 * Created by zoupeng on 16/3/3.
 */
public class NodeServiceBuilder {

    public void build(Node node){
        serviceBuilder(node);
    }

    private void serviceBuilder(Node node){
        for(Service service : node.getServices()){
            service.setNode(node);
            operationBuilder(service);
        }
    }

    private void operationBuilder(Service service){
        for(Operation operation : service.getOperations()){
            operation.setService(service);
            paramBuilder(operation);
        }
    }

    private void paramBuilder(Operation operation){
        for(Parameter parameter : operation.getParams()){
            parameter.setOperation(operation);
        }
    }
}
