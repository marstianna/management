package com.dmall.management.test;

import com.dmall.management.core.invoker.ReflectInvoker;
import com.dmall.management.core.parser.NodeParser;
import com.dmall.management.core.NodeServiceBuilder;
import com.dmall.management.core.bean.Node;
import com.dmall.management.core.bean.Operation;
import com.dmall.management.core.bean.Parameter;
import com.dmall.management.core.bean.Service;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by zoupeng on 16/3/3.
 */
public class TestController {
    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("spring.xml");
        NodeParser nodeParser = ac.getBean(NodeParser.class);
        Node node = nodeParser.parse();
        NodeServiceBuilder nodeServiceBuilder = new NodeServiceBuilder();
        nodeServiceBuilder.build(node);
        for(Service service : node.getServices()){
            for(Operation operation : service.getOperations()){
                for(Parameter parameter: operation.getParams()){
                    parameter.setValue("\"all\"");
                }
                new ReflectInvoker().invoke(operation);
            }
        }
    }
}
