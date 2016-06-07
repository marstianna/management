package com.dmall.managed.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmall.managed.core.Resource;
import com.dmall.managed.core.bean.Node;
import com.dmall.managed.core.helper.HttpSender;
import com.dmall.managed.core.server.impl.AbstractNodeServiceImpl;
import org.apache.commons.httpclient.NameValuePair;

import java.util.ArrayList;

/**
 * Created by zoupeng on 16/4/7.
 */
public abstract class HttpNodeServiceImpl extends AbstractNodeServiceImpl {
    private Resource resource;
    @Override
    public void register(String ip, Integer port, String path) {
        String nodeInfo = HttpSender.connect(ip,port,path, new ArrayList<NameValuePair>());
        JSONObject json = JSON.parseObject(nodeInfo);
        Node node = json.getObject("nodeInfo",Node.class);
        register(node);
    }

}
