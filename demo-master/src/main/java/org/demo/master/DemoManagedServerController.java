package org.demo.master;

import com.dmall.managed.core.server.AbstractManagedServerController;
import com.dmall.managed.core.server.service.NodeService;
import com.google.common.collect.ImmutableBiMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by zoupeng on 16/3/11.
 */
@RequestMapping("/server/manage")
@Controller
public class DemoManagedServerController extends AbstractManagedServerController {
    @Autowired
    NodeService nodeService;

    @Override
    @RequestMapping("/manualRegister")
    @ResponseBody
    public Object manualRegister(String ip, Integer port, String path) {
        nodeService.register(ip, port, path);
        return ImmutableBiMap.of("success",true);
    }

    @Override
    @RequestMapping("/register")
    @ResponseBody
    public Object register(String nodeInfo) {
        return super.register(nodeInfo);
    }

    @Override
    @RequestMapping("/nodeList")
    @ResponseBody
    public Object nodeList() {
        return super.nodeList();
    }

    @Override
    @RequestMapping("/deregister")
    @ResponseBody
    public Object deregister(String nodeQualifier) {
        return super.deregister(nodeQualifier);
    }

    @Override
    @RequestMapping("/exec")
    @ResponseBody
    public Object exec(String nodeQualifier, String operationQualifier, Map<String, Object> params) {
        return super.exec(nodeQualifier, operationQualifier, params);
    }

    @Override
    @RequestMapping("/batchExec")
    @ResponseBody
    public Object batchExec(String operationQualifier, Map<String, Object> params) {
        return super.batchExec(operationQualifier, params);
    }

    @Override
    public NodeService getNodeService() {
        return nodeService;
    }
}
