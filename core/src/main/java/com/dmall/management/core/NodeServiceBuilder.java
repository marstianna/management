package com.dmall.management.core;

import com.dmall.management.core.bean.Node;
import com.dmall.management.core.bean.Operation;
import com.dmall.management.core.bean.Parameter;
import com.dmall.management.core.bean.Service;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Node节点由于在生成的时候有循环引用,导致json数据库过场,
 * 所以,在生成Node节点的时候,去掉循环引用,在master接受到
 * 注册请求后,重新完善这个节点
 *
 * Created by zoupeng on 16/3/3.
 */
public class NodeServiceBuilder {

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
}
