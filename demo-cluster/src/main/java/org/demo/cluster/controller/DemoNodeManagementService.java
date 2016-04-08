package org.demo.cluster.controller;

import com.alibaba.fastjson.JSONObject;
import com.dmall.managed.core.client.ManagementClientServiceImpl;
import com.dmall.managed.core.client.NodeClient;
import com.dmall.managed.core.Invoker;
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
public class DemoNodeManagementService extends ManagementClientServiceImpl {
    @Autowired
    DemoManagementService demoManagementService;
    @Resource(name = "springInvoker")
    Invoker invoker;

    @RequestMapping("exec")
    @ResponseBody
    @Override
    public Object exec(String operationQualifier, @ModelAttribute Map<String,Object> params){
        return super.exec(operationQualifier, params);
    }

    @RequestMapping("register")
    @ResponseBody
    public String getNodeInfo() {
        return JSONObject.toJSONString(demoManagementService.getNode());
    }

    @Override
    public Invoker getInvoker() {
        return invoker;
    }

    @Override
    public NodeClient getNodeClient() {
        return demoManagementService;
    }
}
