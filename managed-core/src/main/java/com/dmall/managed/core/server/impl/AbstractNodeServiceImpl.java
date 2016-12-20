package com.dmall.managed.core.server.impl;

import com.dmall.managed.core.Invoker;
import com.dmall.managed.core.bean.HealthCheck;
import com.dmall.managed.core.bean.Node;
import com.dmall.managed.core.bean.Operation;
import com.dmall.managed.core.server.NodeService;
import com.dmall.managed.core.server.RegisterStore;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

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
            if(node.getNodeQualifier().contains(uniqueHost)){
                delete = node;
                break;
            }
        }
        if(delete != null) {
            deregister(delete.getNodeQualifier());
        }
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
        List<String> qualifiers= new ArrayList<>();

        List<Operation> operations = registerStore.getOperations(operationQualifier);
        for(final Operation operation : operations){
            final String nodeQualifier = operation.getService().getNode().getNodeQualifier();
            String qualifier = qualifierBuilder(nodeQualifier, operationQualifier);
            batchExecuteService.submit(qualifier,new Callable<Object>() {
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
            qualifiers.add(qualifier);
        }

        Map<String,Object> results = new HashMap<>();
        for(String qualifier : qualifiers){
            Object value;
            try {
                value = batchExecuteService.poll(qualifier,3L, TimeUnit.SECONDS);
            } catch (Exception e){
                LOGGER.error("调用"+qualifier+"失败,请立即检查!!!");
                value = e;
            }
            results.put(qualifier,value);
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
                    healthCheck.setTargetNodeQualifier(decodeTransferNodeQualifier(nodeQualifier));
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

    private String qualifierBuilder(String nodeQualifier,String operationQualifier){
        return nodeQualifier+"-"+operationQualifier+"-"+System.currentTimeMillis();
    }

    private String decodeTransferNodeQualifier(String transferNodeQualifier){
        String parts[] = transferNodeQualifier.split("-");
        if(parts.length == 3){
            return parts[0];
        }else{
            throw new RuntimeException("transferNodeQualifier出现异常:"+transferNodeQualifier);
        }
    }
}
