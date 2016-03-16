package com.dmall.managed.core.server.invoker;

import com.alibaba.fastjson.JSON;
import com.dmall.managed.core.Invoker;
import com.dmall.managed.core.bean.Node;
import com.dmall.managed.core.bean.Operation;
import com.dmall.managed.core.helper.HttpSender;
import org.apache.commons.httpclient.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zoupeng on 16/3/2.
 */
public class HttpInvoker implements Invoker {

    protected static final Logger LOGGER = LoggerFactory.getLogger(HttpInvoker.class);

    public Object invoke(Operation operation, Map<String,Object> params) {
        LOGGER.info("httpInvoker接受到执行请求"+operation.getQualifier()+",开始执行");
        List<NameValuePair> paramList = makeParams(operation, params);
        Node node = operation.getService().getNode();
        return HttpSender.connect(node.getIp(),node.getPort(),node.getBasePath(), paramList);
    }

    private List<NameValuePair> makeParams(Operation operation , Map<String,Object> params){
        List<NameValuePair> paramList = new ArrayList<>();
        for(String key : params.keySet()){
            NameValuePair value = new NameValuePair(key, JSON.toJSONString(params.get(key)));
            paramList.add(value);
        }
        paramList.add(new NameValuePair("operationQualifier",operation.getQualifier()));
        return paramList;
    }
}
