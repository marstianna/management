package com.dmall.managed.core.server;

import com.alibaba.fastjson.JSON;
import com.dmall.managed.core.NodeServiceBuilder;
import com.dmall.managed.core.bean.Node;
import com.dmall.managed.core.server.service.NodeService;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by zoupeng on 16/3/10.
 */
public abstract class AbstractManagedServerController {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractManagedServerController.class);

    public Object register(String nodeInfo){
        boolean success = false;
        try {
            Node node = JSON.parseObject(nodeInfo, Node.class);
            LOGGER.info("接收到"+node.getNodeQualifier()+"("+node.getGroup()+")的注册请求!!!");
            NodeServiceBuilder.buildNode(node);
            getNodeService().register(node);
            success = true;
        }catch (Exception e){
            LOGGER.error("节点注册失败,请检查注册字符串是否正确",e);
        }
        return ImmutableMap.of("success",success);
    }

    public Object nodeList(){
        return getNodeService().getNodes();
    }

    public Object deregister(String nodeQualifier){
        LOGGER.info("解除"+nodeQualifier+"注册");
        getNodeService().deregister(nodeQualifier);
        return ImmutableMap.of("success",true);
    }

    public Object exec(String nodeQualifier,String operationQualifier,Map<String,Object> params){
        LOGGER.info("即将执行节点"+nodeQualifier+"的操作请求("+operationQualifier+"),参数为"+params);
        return getNodeService().exec(nodeQualifier, operationQualifier, params);
    }

    public Object batchExec(String operationQualifier,Map<String,Object> params){
        LOGGER.info("即将执行节点所有节点的操作请求("+operationQualifier+"),参数为"+params);
        Map<String,Object> results = getNodeService().batchExec(operationQualifier,params);
        return results;
    }

    public abstract Object manualRegister(String ip,Integer port,String path);

    public abstract NodeService getNodeService();


}
