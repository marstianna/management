package com.dmall.managed.core.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmall.managed.core.Invoker;
import com.dmall.managed.core.bean.Node;
import com.dmall.managed.core.bean.Operation;
import com.dmall.managed.core.helper.HttpSender;
import com.dmall.managed.core.server.service.BatchExecuteService;
import com.dmall.managed.core.server.service.NodeService;
import com.dmall.managed.core.server.service.RegisterStore;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.httpclient.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Created by zoupeng on 16/3/10.
 */
public class NodeServiceImpl implements NodeService {
    private RegisterStore registerStore;
    private Invoker invoker;
    private BatchExecuteService batchExecuteService;

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
    public List<Object> batchExec(final String operationQualifier, final Map<String, Object> params) {
        List<Future<Object>> results= new ArrayList<>();

        List<Operation> operations = registerStore.getOperations(operationQualifier);
        for(final Operation operation : operations){
            Future<Object> result = batchExecuteService.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    Object result = null;
                    try {
                        result = exec(operation, params);
                    } catch (Exception e) {
                        LOGGER.error("执行operation:"+operationQualifier+",节点为:"+operation.getService().getNode().getNodeQualifier(),e);
                    }
                    return result;
                }
            });
            results.add(result);
        }

        return Lists.transform(results, new Function<Future<Object>, Object>() {
            @Override
            public Object apply(Future<Object> input) {
                try {
                    return input.get();
                } catch (Exception e){
                    LOGGER.error("执行失败,请立即检查");
                }
                return null;
            }
        });
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

    public BatchExecuteService getBatchExecuteService() {
        return batchExecuteService;
    }

    public void setBatchExecuteService(BatchExecuteService batchExecuteService) {
        this.batchExecuteService = batchExecuteService;
    }
}
