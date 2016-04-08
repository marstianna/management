package com.dmall.managed.server;

import com.dmall.managed.core.bean.Node;
import com.dmall.managed.core.client.ManagementClientService;
import com.dmall.managed.core.server.service.impl.AbstractNodeServiceImpl;

/**
 * Created by zoupeng on 16/4/7.
 */
public abstract class DubboNodeServiceImpl extends AbstractNodeServiceImpl {

    @Override
    public void register(String ip, Integer port, String path) {
        ManagementClientService managementClientService = DubboServiceReferenceHelper.get(ip, port, path);
        Node node = managementClientService.registerNodeInfo();
        register(node);
    }

}
