package org.demo.cluster.management.impl;

import com.alibaba.fastjson.JSON;
import com.dmall.management.core.helper.HttpSender;
import com.dmall.management.core.parser.NodeParser;
import com.google.common.collect.Lists;
import org.apache.commons.httpclient.NameValuePair;
import org.demo.cluster.management.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zoupeng on 16/3/3.
 */
@Service
public class RegisterServiceImpl implements RegisterService{
    @Autowired
    NodeParser nodeParser;

//    @PostConstruct
    public void register() {
        NameValuePair pair = new NameValuePair("node", JSON.toJSONString(nodeParser.get()));
        HttpSender.connect("192.168.8.44",8081,"/node/management/register", Lists.newArrayList(pair));
    }
}
