package com.dmall.management.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmall.management.core.NodeCache;
import com.dmall.management.core.NodeServiceBuilder;
import com.dmall.management.core.bean.Node;
import com.dmall.management.core.helper.HttpSender;
import com.dmall.management.core.service.ManageService;

/**
 * Created by zoupeng on 16/3/4.
 */
public class ManageServiceImpl implements ManageService {
    private NodeCache nodeCache;

    public void manualRegister(String ip,Integer port, String path) {
        String result = HttpSender.connect(ip,port,path,null);
        JSONObject obj = JSON.parseObject(result);
        Node node = null;
        if(obj.getString("success").equals("true")){
            try {
                node = obj.getObject("nodeInfo",Node.class);
                node.setNodeQualifier(nodeQualifierBuilder(node));
                NodeServiceBuilder builder = new NodeServiceBuilder();
                builder.build(node);
                nodeCache.register(node);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void flushNodeInCluster(String ip, Integer port, String path) {
        String result = HttpSender.connect(ip,port,path,null);
        JSONObject obj = JSON.parseObject(result);
        if(!obj.getString("success").equals("true")){
            throw new RuntimeException("刷新"+ip+":"+port+path+"失败,请重试");
        }
    }

    public void deregister(String nodeQualifier){
        nodeCache.deregister(nodeQualifier);
    }

    public String nodeQualifierBuilder(Node node) {
        return node.getIp()+":"+node.getPort()+"/"+node.getDisplayName();
    }

    public NodeCache getNodeCache() {
        return nodeCache;
    }

    public void setNodeCache(NodeCache nodeCache) {
        this.nodeCache = nodeCache;
    }


}
