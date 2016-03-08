package com.dmall.management.core.parser;

import com.dmall.management.core.NodeServiceBuilder;
import com.dmall.management.core.bean.Node;
import com.dmall.management.core.bean.Operation;
import com.dmall.management.core.bean.Service;
import com.dmall.management.core.configuration.ManagementConfig;
import com.dmall.management.core.scanner.ManagementServiceScanner;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by zoupeng on 16/3/2.
 */
public class NodeParser {
    private ManagementConfig managementConfig;

    private AtomicReference<Node> nodeRefer = new AtomicReference<Node>();

    public Node get(){
        if(nodeRefer.get() != null){
            return nodeRefer.get();
        }

        updateImmediately();

        return  nodeRefer.get();
    }

    public void updateImmediately(){
        synchronized (nodeRefer) {
            if (managementConfig == null || managementConfig.getClassNames().size() == 0) {
                return;
            }
            Node node = managementConfig.getNode();

            for (String className : managementConfig.getClassNames()) {
                ManagementServiceScanner serviceScanner = ManagementServiceScanner.getInstance(className);
                Service service = serviceScanner.scan();
                if (service != null) {
                    node.add(service);
                }
            }

            node.setNodeQualifier(NodeServiceBuilder.buildNodeQualifier(node));

            nodeRefer.set(node);
        }
    }

    public Operation getOperation(String operationQualifier){
        Node node = get();

        Operation target = null;
        for(Service service : node.getServices()){
            for(Operation operation : service.getOperations()){
                if(operation.getQualifier().equals(operationQualifier)){
                    operation.setService(service);
                    service.setNode(node);
                    target = operation;
                }
            }
        }

        return target;
    }

    public ManagementConfig getManagementConfig() {
        return managementConfig;
    }

    public void setManagementConfig(ManagementConfig managementConfig) {
        this.managementConfig = managementConfig;
    }
}
