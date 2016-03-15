package com.dmall.managed.core.server;

import com.alibaba.fastjson.JSON;
import com.dmall.managed.core.NodeServiceBuilder;
import com.dmall.managed.core.bean.Node;
import com.dmall.managed.core.server.service.NodeService;
import com.google.common.collect.ImmutableBiMap;

import java.util.List;
import java.util.Map;

/**
 * Created by zoupeng on 16/3/10.
 */
public abstract class AbstractManagedServerController {

    public Object register(String nodeInfo){
        Node node = JSON.parseObject(nodeInfo,Node.class);
        NodeServiceBuilder.buildNode(node);
        getNodeService().register(node);
        return ImmutableBiMap.of("success",true);
    }

    public Object nodeList(){
        return getNodeService().getNodes();
    }

    public Object deregister(String nodeQualifier){
        getNodeService().deregister(nodeQualifier);
        return ImmutableBiMap.of("success",true);
    }

    public Object exec(String nodeQualifier,String operationQualifier,Map<String,Object> params){
        return getNodeService().exec(nodeQualifier, operationQualifier, params);
    }

    public Object batchExec(String operationQualifier,Map<String,Object> params){
        List<Object> results = getNodeService().batchExec(operationQualifier,params);
        return results;
    }

    public abstract Object manualRegister(String ip,Integer port,String path);

    public abstract NodeService getNodeService();


}
