package com.dmall.management.core.invoker;

import com.alibaba.fastjson.JSON;
import com.dmall.management.core.Invoker;
import com.dmall.management.core.NodeCache;
import com.dmall.management.core.NodeServiceBuilder;
import com.dmall.management.core.bean.Node;
import com.dmall.management.core.bean.Operation;
import com.dmall.management.core.helper.HttpSender;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

/**
 * Created by zoupeng on 16/3/2.
 */
public class HttpInvoker implements Invoker{
    private NodeCache nodeCache;

    public Object invoke(String nodeQualifier, String operationQualifier, Map<String,Object> params) {
        Operation operation = nodeCache.getOperation(nodeQualifier,operationQualifier);
        Pair<String,Integer> address = NodeServiceBuilder.parserNodeQualifier(nodeQualifier);

        NameValuePair[] tmp = new NameValuePair[params.size()];
        int i = 0;
        for(String key : params.keySet()){
            NameValuePair value = new NameValuePair(key, JSON.toJSONString(params.get(key)));
            tmp[i] = value;
            i++;
        }

        Node node = operation.getService().getNode();
        return HttpSender.connect(node.getIp(),node.getPort(),node.getBasePath(),tmp);
    }

    public NodeCache getNodeCache() {
        return nodeCache;
    }

    public void setNodeCache(NodeCache nodeCache) {
        this.nodeCache = nodeCache;
    }
}
