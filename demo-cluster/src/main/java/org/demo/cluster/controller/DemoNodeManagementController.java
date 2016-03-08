package org.demo.cluster.controller;

import com.dmall.management.core.Invoker;
import com.dmall.management.core.bean.Node;
import com.dmall.management.core.parser.NodeParser;
import org.demo.cluster.management.DemoManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by zoupeng on 16/3/3.
 */
@Controller
@RequestMapping("/node/management/")
public class DemoNodeManagementController {
    @Autowired
    DemoManagementService demoManagementService;
    @Autowired
    NodeParser nodeParser;
    @Resource(name = "springInvoker")
    Invoker invoker;

    @RequestMapping("register")
    @ResponseBody
    public Node register(){
        return nodeParser.get();
    }

    @RequestMapping("exec")
    @ResponseBody
    public Object exec(String nodeQualifier, String operationQualifier, @ModelAttribute Map<String,Object> params){
//        return invoker.invoke(operation);
        return invoker.invoke(nodeQualifier,operationQualifier,params);
    }
}
