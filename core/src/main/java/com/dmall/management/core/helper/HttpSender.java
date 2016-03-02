package com.dmall.management.core.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by zoupeng on 16/2/24.
 */
public class HttpSender {
    protected static final Logger LOGGER = LoggerFactory.getLogger(HttpSender.class);


    public static Pair<Boolean,String> connect(String ip,Integer servingPort,String servingPath,List<NameValuePair> params){
        String address = ip +":" + servingPort;

        HttpClient client = new HttpClient();

        PostMethod method = new PostMethod();
        method.addParameter("ip",ip);
        method.addParameter("servingPort",String.valueOf(servingPort));
        method.addParameters(params.toArray(new NameValuePair[params.size()]));

        String requestUrl = "http://"+address+"/"+servingPath;
        try{

            URI uri = new URI(requestUrl,false);
            method.setURI(uri);
            client.executeMethod(method);
            String registerResult = method.getResponseBodyAsString();

            JSONObject result = JSON.parseObject(registerResult);

            Boolean success = result.getBoolean("success");
            String data = result.getString("data");

            return Pair.of(success,data);
        }catch (IOException e){
            LOGGER.error("Connect ("+address+") Failed : " + e.getMessage(), e);
            throw new RuntimeException("连接服务器失败",e);
        }catch (Exception e){
            throw new RuntimeException("连接地址:"+requestUrl,e);
        }
    }

}
