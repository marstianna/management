package com.dmall.managed.core.client;

import com.dmall.managed.core.Invoker;
import com.dmall.managed.core.bean.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by zoupeng on 16/3/11.
 */
public class ManagementClientServiceImpl implements ManagementClientService{
    private Invoker invoker;
    private NodeClient nodeClient;

    protected static final Logger LOGGER = LoggerFactory.getLogger(ManagementClientServiceImpl.class);

    @Override
    public Object exec(String operationQualifier,Map<String,Object> params){
        LOGGER.info("开始执行:"+operationQualifier+", 等待执行结果!!!!");
        return getInvoker().invoke(getNodeClient().findOperation(operationQualifier),params);
    }

    @Override
    public Node registerNodeInfo() {
        return nodeClient.getNode();
    }

    @Override
    public Invoker getInvoker() {
        return invoker;
    }

    @Override
    public void setInvoker(Invoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public NodeClient getNodeClient() {
        return nodeClient;
    }

    @Override
    public void setNodeClient(NodeClient nodeClient) {
        this.nodeClient = nodeClient;
    }
}
