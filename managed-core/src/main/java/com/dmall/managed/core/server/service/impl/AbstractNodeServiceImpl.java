package com.dmall.managed.core.server.service.impl;

import com.dmall.managed.core.Invoker;
import com.dmall.managed.core.bean.HealthCheck;
import com.dmall.managed.core.bean.Node;
import com.dmall.managed.core.bean.Operation;
import com.dmall.managed.core.server.service.iface.NodeService;
import com.dmall.managed.core.server.service.iface.RegisterStore;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Created by zoupeng on 16/3/10.
 */
public abstract class AbstractNodeServiceImpl implements NodeService {
    protected RegisterStore registerStore;
    protected Invoker invoker;
    protected BatchExecuteService batchExecuteService;

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractNodeServiceImpl.class);

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

        deregister(delete.getNodeQualifier());
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
    public Map<String,Object> batchExec(final String operationQualifier, final Map<String, Object> params) {
        Map<String,Future<Object>> maps= new HashMap<>();

        List<Operation> operations = registerStore.getOperations(operationQualifier);
        for(final Operation operation : operations){
            final String nodeQualifier = operation.getService().getNode().getNodeQualifier();
            Future<Object> result = batchExecuteService.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    Object received = null;
                    try {
                        received = exec(operation, params);
                    } catch (Exception e) {
                        LOGGER.error("执行operation:"+operationQualifier+",节点为:"+operation.getService().getNode().getNodeQualifier(),e);
                    }
                    return received;
                }
            });
            maps.put(nodeQualifier,result);
        }

        Map<String,Object> results = new HashMap<>();
        for(String nodeQualifier : maps.keySet()){
            Object value;
            try {
                value = maps.get(nodeQualifier).get();
            } catch (Exception e){
                LOGGER.error("调用"+nodeQualifier+"失败,请立即检查!!!");
                value = e;
            }
            results.put(nodeQualifier,value);
        }

        return results;
    }

    @Override
    public Pair<String,? extends Map<String,String>> healthCheck(HealthCheck healthCheck) {
        Pair<String,? extends Map<String,String>> pair = Pair.of(healthCheck.getDefaultValue(),new HashMap<String,String>());
        if(StringUtils.isBlank(healthCheck.getTargetNodeQualifier())){
            try {
                Map<String, Object> results = batchExec(healthCheck.getTargetOperationQualifier(), null);
                for (String nodeQualifier : results.keySet()) {
                    Object result = results.get(nodeQualifier);
                    healthCheck.selfCheck(result);
                    pair.getValue().put(nodeQualifier, healthCheck.getCurrentValue());
                }
            }catch (Exception e){
                LOGGER.error("healthCheck失败,调用方法:"+healthCheck.getTargetOperationQualifier(),e);
                healthCheck.setInvokeException(e);
            }
        }else{
            try {
                Object result = exec(healthCheck.getTargetNodeQualifier(), healthCheck.getTargetOperationQualifier(), null);
                healthCheck.selfCheck(result);
                pair.getValue().put(healthCheck.getTargetNodeQualifier(), healthCheck.getCurrentValue());
            }catch (Exception e){
                LOGGER.error("healthCheck失败,调用节点:"+healthCheck.getTargetNodeQualifier()+",调用方法:"+healthCheck.getTargetOperationQualifier(),e);
                healthCheck.setInvokeException(e);
            }
        }
        return pair;
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
