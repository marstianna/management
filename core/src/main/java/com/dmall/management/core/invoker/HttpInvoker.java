package com.dmall.management.core.invoker;

import com.dmall.management.core.bean.Node;
import com.dmall.management.core.bean.Operation;
import com.dmall.management.core.bean.Parameter;
import com.dmall.management.core.helper.HttpSender;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zoupeng on 16/3/2.
 */
public class HttpInvoker {

    public Object invoke(Operation operation){
        Node node = operation.getService().getNode();

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for(Parameter parameter : operation.getParams()){
            params.add(new NameValuePair(parameter.getName(),parameter.getValue()));
        }

        Pair<Boolean,String> result = HttpSender.connect(node.getIp(),node.getPort(),node.getBasePath()+"/"+operation.getPath(),params);
        return result;
    }
}
