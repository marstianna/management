package com.dmall.managed.core.server.impl;

import com.alibaba.fastjson.JSON;
import com.dmall.managed.core.NodeServiceBuilder;
import com.dmall.managed.core.bean.HealthCheck;
import com.dmall.managed.core.bean.Node;
import com.dmall.managed.core.server.ManagementServerService;
import com.dmall.managed.core.server.NodeService;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by zoupeng on 16/3/10.
 */
public class ManagementServerServiceImpl implements ManagementServerService {
    private NodeService nodeService;

    protected static final Logger LOGGER = LoggerFactory.getLogger(ManagementServerServiceImpl.class);

    @Override
    public void register(String nodeInfo){
        try {
            Node node = JSON.parseObject(nodeInfo, Node.class);
            LOGGER.info("接收到"+node.getNodeQualifier()+"("+node.getGroup()+")的注册请求!!!");
            NodeServiceBuilder.buildNode(node);
            nodeService.register(node);
        }catch (Exception e){
            LOGGER.error("节点注册失败,请检查注册字符串是否正确",e);
        }
    }

    @Override
    public Map<String, List<Node>> nodeList(){
        return getNodeService().getNodes();
    }

    @Override
    public void logout(String nodeQualifier){
        LOGGER.info("解除"+nodeQualifier+"注册");
        getNodeService().deregister(nodeQualifier);
    }

    @Override
    public Object exec(String nodeQualifier,String operationQualifier,Map<String,Object> params){
        LOGGER.info("即将执行节点"+nodeQualifier+"的操作请求("+operationQualifier+"),参数为"+params);
        return getNodeService().exec(nodeQualifier, operationQualifier, params);
    }

    @Override
    public Map<String,Object> batchExec(String operationQualifier,Map<String,Object> params){
        LOGGER.info("即将执行节点所有节点的操作请求("+operationQualifier+"),参数为"+params);
        return getNodeService().batchExec(operationQualifier,params);
    }

    @Override
    public void manualRegister(String ip, Integer port, String path) {
        Preconditions.checkArgument(!StringUtils.isBlank(ip) && port != null && !StringUtils.isBlank(path),
                "传入参数非法,请检查:ip:"+ip+",port = "+port + ", path" + path);

        nodeService.register(ip,port,path);
    }

    @Override
    public synchronized void refresh() {
        nodeService.refresh();
    }

    @Override
    public Pair<String, ? extends Map<String, String>> healthCheck(HealthCheck healthCheck) {
        Preconditions.checkArgument(healthCheck != null);
        return nodeService.healthCheck(healthCheck);
    }

    @Override
    public NodeService getNodeService() {
        return this.nodeService;
    }

    @Override
    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }
}
