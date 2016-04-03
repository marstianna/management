package com.dmall.managed.core;

import com.dmall.managed.core.annotaion.ManagementOperation;
import com.dmall.managed.core.annotaion.ManagementParam;
import com.dmall.managed.core.annotaion.ManagementParams;
import com.dmall.managed.core.annotaion.ManagementService;
import com.dmall.managed.core.bean.Node;
import com.dmall.managed.core.bean.Operation;
import com.dmall.managed.core.bean.Parameter;
import com.dmall.managed.core.bean.Service;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Node节点由于在生成的时候有循环引用,导致json数据库过场,
 * 所以,在生成Node节点的时候,去掉循环引用,在master接受到
 * 注册请求后,重新完善这个节点
 *
 * Created by zoupeng on 16/3/3.
 */
public class NodeServiceBuilder {

    public static Service buidNodeService(Class<?> c){
        Service service = new Service();
        service.setName(c.getName());
        ManagementService managementService = c.getAnnotation(ManagementService.class);
        service.setDesc(managementService.desc());
        service.setOperations(getOperationsByService(c));
        return service;
    }

    public static String buildNodeQualifier(Node node){
        return node.getIp()+":"+node.getPort()+"/"+node.getDisplayName();
    }

    public static Pair<String,Integer> parserNodeQualifier(String nodeQualifier){
        String[] displays = nodeQualifier.split("/");
        String[] address = displays[0].split(":");
        return Pair.of(address[0],Integer.parseInt(address[1]));
    }


    public static void buildNode(Node node){
        serviceBuilder(node);
    }

    private static void serviceBuilder(Node node){
        for(Service service : node.getServices()){
            service.setNode(node);
            operationBuilder(service);
        }
    }

    private static void operationBuilder(Service service){
        for(Operation operation : service.getOperations()){
            operation.setService(service);
            paramBuilder(operation);
        }
    }

    private static void paramBuilder(Operation operation){
        for(Parameter parameter : operation.getParams()){
            parameter.setOperation(operation);
        }
    }

    private static List<Operation> getOperationsByService(Class<?> c){
        List<Operation> operations = new ArrayList<Operation>();

        Method[] methods = c.getMethods();
        for(Method method : methods){
            Operation operation = new Operation();
            ManagementOperation annotationOfOperation = method.getAnnotation(ManagementOperation.class);
            if(annotationOfOperation == null){
                continue;
            }
            operation.setDisplayName(annotationOfOperation.displayName());
            operation.setQualifier(annotationOfOperation.qualifier());
            operation.setType(annotationOfOperation.type().getName());
            operation.setName(method.getName());
            operation.setParams(getParamsByOperation(method));
            operations.add(operation);
        }

        return operations;
    }

    private static List<Parameter> getParamsByOperation(Method m){
        List<Parameter> params = new ArrayList<>();

        ManagementParams managementParams = m.getAnnotation(ManagementParams.class);
        if(managementParams == null){
            return params;
        }
        ManagementParam[] tmpParams = managementParams.params();
        for(ManagementParam param : tmpParams){
            Parameter parameter = new Parameter();
            parameter.setDesc(param.desc());
            parameter.setName(param.name());
            parameter.setType(param.type());
            parameter.setOrder(param.order());
            parameter.setParamType(param.paramType().getName());
            params.add(parameter);
        }

        return params;
    }
}
