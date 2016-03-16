package com.dmall.managed.core.helper;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by zoupeng on 16/2/24.
 */
public class HttpSender {
    protected static final Logger LOGGER = LoggerFactory.getLogger(HttpSender.class);


    public static String connect(String ip,Integer servingPort,String servingPath,List<NameValuePair> params){

       return connect(ip+":"+servingPort+servingPath, params);
    }

    public static String connect(String host,String servingPath,List<NameValuePair> params){
        return connect(host+servingPath,params);
    }

    public static String connect(String absolutePath,List<NameValuePair> params){
        return connect(absolutePath,params.toArray(new NameValuePair[params.size()]));
    }

    private static String connect(String absolutePath,NameValuePair[] params){
        HttpClient client = new HttpClient();

        PostMethod method = new PostMethod();
        method.addParameters(params);

        String requestUrl = "http://"+absolutePath;

        LOGGER.info("请求连接地址为:"+requestUrl);
        try{
            URI uri = new URI(requestUrl,false);
            method.setURI(uri);
            client.executeMethod(method);
            return method.getResponseBodyAsString();
        }catch (IOException e){
            LOGGER.error("Connect ("+absolutePath+") Failed : " + e.getMessage(), e);
            throw new RuntimeException("连接服务器失败",e);
        }catch (Exception e){
            LOGGER.error("Connect ("+absolutePath+") Failed : " + e.getMessage(), e);
            throw new RuntimeException("连接地址:"+requestUrl,e);
        }
    }
}
