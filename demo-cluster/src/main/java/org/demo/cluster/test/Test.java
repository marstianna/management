package org.demo.cluster.test;

import com.alibaba.fastjson.JSON;
import com.dmall.management.core.NodeCache;
import com.dmall.management.core.NodeServiceBuilder;
import com.dmall.management.core.bean.Node;
import com.dmall.management.core.bean.Operation;
import com.dmall.management.core.bean.Parameter;
import com.dmall.management.core.parser.NodeParser;
import org.demo.cluster.controller.DemoNodeManagementController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

/**
 * Created by zoupeng on 16/3/4.
 */
public class Test {
    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("spring.xml");
        NodeParser parser = ac.getBean(NodeParser.class);
        NodeServiceBuilder builder = new NodeServiceBuilder();
        Node node = parser.get();
        builder.build(node);

        NodeCache cache = new NodeCache();
        cache.register(node);

        DemoNodeManagementController controller = ac.getBean(DemoNodeManagementController.class);

        Map<String,Operation> operations = cache.getOperationByQualified("demo_devide");
        for(String nodeQualifier : operations.keySet()){
            Operation operation = operations.get(nodeQualifier);
            for(Parameter parameter : operation.getParams()){
                if(parameter.getName().equals("x")){
                    parameter.setValue("5");
                }
                if(parameter.getName().equals("y")){
                    parameter.setValue("3");
                }
            }
            Object result = controller.exec(operation);
            System.out.println(JSON.toJSONString(result));
        }

    }
}
