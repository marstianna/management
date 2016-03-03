package org.demo.cluster.management.impl;

import com.alibaba.fastjson.JSON;
import com.dmall.management.core.Invoker;
import com.dmall.management.core.helper.HttpSender;
import com.dmall.management.core.invoker.HttpInvoker;
import com.dmall.management.core.parser.NodeParser;
import com.google.common.collect.Lists;
import org.apache.commons.httpclient.NameValuePair;
import org.demo.cluster.management.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by zoupeng on 16/3/3.
 */
@Service
public class RegisterServiceImpl implements RegisterService{
    Invoker invoker = new HttpInvoker();
    @Autowired
    NodeParser nodeParser;

    @Override
    @PostConstruct
    public void register() {
        NameValuePair pair = new NameValuePair("node", JSON.toJSONString(nodeParser.parse()));
        HttpSender.connect("192.168.8.44",8081,"/node/management/register", Lists.newArrayList(pair));
    }
}
