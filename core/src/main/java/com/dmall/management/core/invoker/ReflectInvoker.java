package com.dmall.management.core.invoker;

import com.alibaba.fastjson.JSON;
import com.dmall.management.core.Invoker;
import com.dmall.management.core.bean.Operation;
import com.dmall.management.core.bean.Parameter;
import com.dmall.management.core.bean.Service;

import java.lang.reflect.Method;

/**
 *
 *
 * Created by zoupeng on 16/3/3.
 */
public class ReflectInvoker implements Invoker{

    public Object invoke(Operation operation){
        Object result = null;
        try {
            Service service = operation.getService();
            Class c = Class.forName(service.getName());
            Object instance = c.newInstance();
            Method method;
            if(operation.getParams() != null && operation.getParams().size()>0){
                Object[] params = new Object[operation.getParams().size()];
                Class<?>[] paramTypes = new Class[operation.getParams().size()];
                int i = 0;
                for(Parameter parameter : operation.getParams()){
                    Object obj = JSON.parseObject(parameter.getValue(),Class.forName(parameter.getType()));
                    params[i] = obj;
                    paramTypes[i] = Class.forName(parameter.getType());
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
