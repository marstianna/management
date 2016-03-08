package com.dmall.management.core.invoker;

import com.alibaba.fastjson.JSON;
import com.dmall.management.core.Invoker;
import com.dmall.management.core.bean.Operation;
import com.dmall.management.core.bean.Parameter;
import com.dmall.management.core.bean.Service;
import com.dmall.management.core.parser.NodeParser;

import java.lang.reflect.Method;
import java.util.Map;

/**
 *
 *
 * Created by zoupeng on 16/3/3.
 */
public class ReflectInvoker implements Invoker{
    private NodeParser nodeParser;

    public Object invoke(String nodeQualifier, String operationQualifier, Map<String,Object> params) {
        Operation operation = nodeParser.getOperation(operationQualifier);
        Object result = null;
        try {
            Service service = operation.getService();
            Class c = Class.forName(service.getName());
            Object instance = c.newInstance();
            Method method;
            if(operation.getParams() != null && operation.getParams().size()>0){
                Object[] tmpParams = new Object[params.size()];
                Class<?>[] paramTypes = new Class[params.size()];
                for(Parameter parameter : operation.getParams()){
                    Object obj = JSON.parseObject(parameter.getValue(),Class.forName(parameter.getType()));
                    tmpParams[parameter.getOrder()] = obj;
                    paramTypes[parameter.getOrder()] = Class.forName(parameter.getType());
                }
                method = c.getMethod(operation.getName(), paramTypes);
                result = method.invoke(instance,params);
            }else{
                method = c.getMethod(operation.getName());
                result = method.invoke(instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public NodeParser getNodeParser() {
        return nodeParser;
    }

    public void setNodeParser(NodeParser nodeParser) {
        this.nodeParser = nodeParser;
    }
}
