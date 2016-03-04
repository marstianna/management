package com.dmall.management.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.dmall.management.core.Const;
import com.dmall.management.core.Resource;
import com.dmall.management.core.helper.HttpSender;
import com.dmall.management.core.parser.NodeParser;
import com.dmall.management.core.service.RegisterService;
import com.google.common.collect.Lists;
import org.apache.commons.httpclient.NameValuePair;

/**
 * Created by zoupeng on 16/3/4.
 */
public class RegisterServiceImpl implements RegisterService {
    private NodeParser nodeParser;
    private Resource resource;

    public void register(String path) {
        String host = resource.get(Const.MASTER_HOST_KEY);
        HttpSender.connect(host,path, Lists.newArrayList(new NameValuePair("nodeInfo", JSON.toJSONString(nodeParser)),
                                                         new NameValuePair("success","true")));
    }

    public NodeParser getNodeParser() {
        return nodeParser;
    }

    public void setNodeParser(NodeParser nodeParser) {
        this.nodeParser = nodeParser;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
