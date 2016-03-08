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

import java.util.HashMap;
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
        builder.buildNode(node);

        NodeCache cache = new NodeCache();
        cache.register(node);

        DemoNodeManagementController controller = ac.getBean(DemoNodeManagementController.class);

        Map<String,Operation> operations = cache.getOperationByQualified("demo_devide");
        Map<String,Object> params = new HashMap<String, Object>();
        for(String nodeQualifier : operations.keySet()){
            Operation operation = operations.get(nodeQualifier);
            int i = 5;
            for(Parameter parameter : operation.getParams()){
                params.put(parameter.getName(),i++);
            }
            Object result = controller.exec(nodeQualifier,operation.getQualifier(),params);
            System.out.println(JSON.toJSONString(result));
        }

    }
}
