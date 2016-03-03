package com.dmall.management.core.parser;

import com.dmall.management.core.configuration.ManagementConfig;
import com.dmall.management.core.scanner.ManagementServiceScanner;
import com.dmall.management.core.bean.Node;
import com.dmall.management.core.bean.Service;

/**
 * Created by zoupeng on 16/3/2.
 */
public class NodeParser {
    private ManagementConfig managementConfig;

    public Node parse(){
        if(managementConfig == null || managementConfig.getClassNames().size() == 0){
            return null;
        }
        Node node = managementConfig.getNode();

        for (String className : managementConfig.getClassNames()){
            ManagementServiceScanner serviceScanner = ManagementServiceScanner.getInstance(className);
            Service service = serviceScanner.scan();
            if(service != null) {
                node.add(service);
            }
        }

        return node;
    }

    public ManagementConfig getManagementConfig() {
        return managementConfig;
    }

    public void setManagementConfig(ManagementConfig managementConfig) {
        this.managementConfig = managementConfig;
    }
}
