package com.dmall.managed.core.client;

import com.dmall.managed.core.Invoker;

import java.util.Map;

/**
 * Created by zoupeng on 16/3/11.
 */
public abstract class AbstractManagedClientController {

    public Object exec(String operationQualifier,Map<String,Object> params){
        return getInvoker().invoke(getNodeClient().findOperation(operationQualifier),params);
    }

    public abstract String getNodeInfo();

    public abstract Invoker getInvoker() ;

    public abstract NodeClient getNodeClient();
}
