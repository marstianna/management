package com.dmall.managed.core.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmall.managed.core.Invoker;
import com.dmall.managed.core.bean.Node;
import com.dmall.managed.core.bean.Operation;
import com.dmall.managed.core.helper.HttpSender;
import com.dmall.managed.core.server.service.NodeService;
import com.dmall.managed.core.server.service.RegisterStore;
import com.google.common.base.Preconditions;
import org.apache.commons.httpclient.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zoupeng on 16/3/10.
 */
public class NodeServiceImpl implements NodeService {
    private RegisterStore registerStore;
    private Invoker invoker;

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeServiceImpl.class);

    @Override
    public void register(String ip, Integer port, String path) {
        String nodeInfo = HttpSender.connect(ip,port,path, new ArrayList<NameValuePair>());
        JSONObject json = JSON.parseObject(nodeInfo);
        Node node = json.getObject("nodeInfo",Node.class);
        register(node);
    }

    @Override
    public void register(Node node) {
        Preconditions.checkArgument(node != null);

        registerStore.store(node);
    }

    @Override
    public void deregister(String ip, Integer port) {
        List<Node> nodes = registerStore.getNodes();
        String uniqueHost = ip+":"+port;

        Node delete = null;
        for(Node node : nodes){
            if(node.getNodeQualifier().indexOf(uniqueHost) != -1){
                delete = node;
                break;
            }
        }

        registerStore.delete(delete.getNodeQualifier());
    }

    @Override
    public void deregister(String nodeQualifier) {
        registerStore.delete(nodeQualifier);
    }

    @Override
    public Node getNode(String nodeQualifier) {
        return registerStore.getNode(nodeQualifier);
    }

    @Override
    public Map<String, List<Node>> getNodes() {
        return registerStore.getNodesGroupByGroupName();
    }

    @Override
    public List<Node> getNodesByGroup(String group) {
        return registerStore.getNodesByGroup(group);
    }

    @Override
    public Object exec(Operation operation, Map<String, Object> params) {
        return invoker.invoke(operation,params);
    }

    @Override
    public Object exec(String nodeQualifier, String operationQualifier, Map<String, Object> params) {
        Operation operation = registerStore.getOperation(nodeQualifier, operationQualifier);
        return exec(operation,params);
    }

    @Override
    public List<Object> batchExec(String operationQualifier, Map<String, Object> params) {
        List<Object> results= new ArrayList<>();

        List<Operation> operations = registerStore.getOperations(operationQualifier);
        for(Operation operation : operations){
            try {
                Object result = exec(operation, params);
                results.add(result);
            } catch (Exception e) {
                LOGGER.error("执行operation:"+operationQualifier+",节点为:"+operation.getService().getNode().getNodeQualifier(),e);
            }
        }

        return results;
    }

    public RegisterStore getRegisterStore() {
        return registerStore;
    }

    public void setRegisterStore(RegisterStore registerStore) {
        this.registerStore = registerStore;
    }

    public Invoker getInvoker() {
        return invoker;
    }

    public void setInvoker(Invoker invoker) {
        this.invoker = invoker;
    }
}
