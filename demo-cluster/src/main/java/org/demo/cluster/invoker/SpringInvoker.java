package org.demo.cluster.invoker;

import com.alibaba.fastjson.JSON;
import com.dmall.management.core.Invoker;
import com.dmall.management.core.bean.Operation;
import com.dmall.management.core.bean.Parameter;
import com.dmall.management.core.bean.Service;
import com.dmall.management.core.parser.NodeParser;
import org.demo.cluster.util.ApplicationContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by zoupeng on 16/3/3.
 */
@Component("springInvoker")
public class SpringInvoker implements Invoker {
    @Autowired
    private NodeParser nodeParser;

    public Object invoke(String nodeQualifier, String operationQualifier, Map<String, Object> params) {
        Operation operation = nodeParser.getOperation(operationQualifier);
        Object result = null;
        try {
            Service service = operation.getService();
            Class c = Class.forName(service.getName());
            Object instance = ApplicationContextUtil.getBean(c);
            Method method;
            if(operation.getParams() != null && operation.getParams().size()>0){
                Object[] tmpParams = new Object[params.size()];
                Class<?>[] paramTypes = new Class[params.size()];
                for(Parameter parameter : operation.getParams()){
                    Object obj = JSON.parseObject(String.valueOf(params.get(parameter.getName())),Class.forName(parameter.getType()));
                    tmpParams[parameter.getOrder()] = obj;
                    paramTypes[parameter.getOrder()] = Class.forName(parameter.getType());
                }
                method = c.getMethod(operation.getName(), paramTypes);
                result = method.invoke(instance,tmpParams);
            }else{
                method = c.getMethod(operation.getName());
                result = method.invoke(instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
