package com.dmall.managed.core.impl;

import com.alibaba.fastjson.JSON;
import com.dmall.managed.core.Resource;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by zoupeng on 16/4/7.
 */
@Deprecated
public class ZkResource implements Resource {
    private String zkAddress;
    private String basepath;
    private RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
    private CuratorFramework client;
    protected static final Logger LOGGER = LoggerFactory.getLogger(ZkResource.class);

    public ZkResource(String _zkAddress){
        this.zkAddress = _zkAddress;
        client = CuratorFrameworkFactory.builder().connectString(this.zkAddress)
                .sessionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
    }

    @Override
    public String get(String key) {
        try {
            List<String> object = client.getChildren().forPath(basepath+"/"+key);
            return JSON.toJSONString(object);
        } catch (Exception e) {
            LOGGER.error("调用Zookeeper服务失败,请求key为"+key);
        }
        return null;
    }

    @Override
    public Integer update(String key, String value) {
        throw new UnsupportedOperationException("ZkResource不支持更新值");
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public String getBasepath() {
        return basepath;
    }

    public void setBasepath(String basepath) {
        this.basepath = basepath;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        ZkResource zk = new ZkResource("192.168.8.236:2181,192.168.8.237:2181,192.168.8.238:2181");
        zk.setBasepath("");
        System.out.println(URLDecoder.decode(zk.get("dubbo/com.dmall.oss.dubbo.OssManagementClientService/providers"),"UTF-8"));
    }
}
