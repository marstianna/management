package com.dmall.managed.core.client;

import com.dmall.managed.core.Invoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by zoupeng on 16/3/11.
 */
public abstract class AbstractManagedClientController {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractManagedClientController.class);

    public Object exec(String operationQualifier,Map<String,Object> params){
        LOGGER.info("开始执行:"+operationQualifier+", 等待执行结果!!!!");
        return getInvoker().invoke(getNodeClient().findOperation(operationQualifier),params);
    }

    public abstract String getNodeInfo();

    public abstract Invoker getInvoker() ;

    public abstract NodeClient getNodeClient();
}
