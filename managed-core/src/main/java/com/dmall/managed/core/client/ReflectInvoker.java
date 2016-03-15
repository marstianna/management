package com.dmall.managed.core.client;

import com.alibaba.fastjson.JSON;
import com.dmall.managed.core.Invoker;
import com.dmall.managed.core.bean.Operation;
import com.dmall.managed.core.bean.Parameter;
import com.dmall.managed.core.bean.Service;

import java.lang.reflect.Method;
import java.util.Map;

/**
 *
 *
 * Created by zoupeng on 16/3/3.
 */
public class ReflectInvoker implements Invoker {

    public Object invoke(Operation operation, Map<String,Object> params) {
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

}
