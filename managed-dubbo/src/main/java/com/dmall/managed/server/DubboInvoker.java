package com.dmall.managed.server;

import com.dmall.managed.core.Invoker;
import com.dmall.managed.core.bean.Node;
import com.dmall.managed.core.bean.Operation;
import com.dmall.managed.core.client.ManagementClientService;

import java.util.Map;

/**
 * Created by zoupeng on 16/4/7.
 */
public class DubboInvoker implements Invoker{
    private ManagementClientService managementClientService;

    @Override
    public Object invoke(Operation operation, Map<String, Object> params) {
        Node node = operation.getService().getNode();
        managementClientService = DubboServiceReferenceHelper.get(node.getIp(),node.getPort(),node.getBasePath());
        return managementClientService.exec(operation.getQualifier(),params);
    }

    public void setManagementClientService(ManagementClientService managementClientService) {
        this.managementClientService = managementClientService;
    }
}
