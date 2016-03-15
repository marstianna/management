package org.demo.cluster.test;

import com.alibaba.fastjson.JSON;
import com.dmall.managed.core.Invoker;
import com.dmall.managed.core.NodeServiceBuilder;
import com.dmall.managed.core.bean.Node;
import org.demo.cluster.invoker.SpringInvoker;
import org.demo.cluster.management.impl.DemoManagementServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zoupeng on 16/3/4.
 */
public class Test {
    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("spring.xml");
        DemoManagementServiceImpl parser = ac.getBean(DemoManagementServiceImpl.class);
        Node node = parser.getNode();
        NodeServiceBuilder.buildNode(node);
        System.out.println(JSON.toJSONString(node));
        Invoker invoker = ac.getBean(SpringInvoker.class);
        Map<String,Object> params = new HashMap<>();
        params.put("x",5);
        params.put("y",3);
        invoker.invoke(node.getServices().get(0).getOperations().get(0),params);
//        NodeServiceBuilder builder = new NodeServiceBuilder();
//        Node node = parser.get();
//        builder.buildNode(node);
//
//        NodeCache cache = new NodeCache();
//        cache.register(node);
//
//        DemoNodeManagementController controller = ac.getBean(DemoNodeManagementController.class);
//
//        Map<String,Operation> operations = cache.getOperationByQualified("demo_devide");
//        Map<String,Object> params = new HashMap<String, Object>();
//        for(String nodeQualifier : operations.keySet()){
//            Operation operation = operations.get(nodeQualifier);
//            int i = 5;
//            for(Parameter parameter : operation.getParams()){
//                params.put(parameter.getName(),i++);
//            }
//            Object result = controller.exec(nodeQualifier,operation.getQualifier(),params);
//            System.out.println(JSON.toJSONString(result));
//        }

    }
}
