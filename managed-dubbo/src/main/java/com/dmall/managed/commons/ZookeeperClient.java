package com.dmall.managed.commons;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by zoupeng on 6/8/16.
 */
public class ZookeeperClient {
    private String zkAddress;
    private String basepath;
    private RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
    private CuratorFramework client;
    protected static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperClient.class);

    public ZookeeperClient(String _zkAddress){
        this.zkAddress = _zkAddress;
        client = CuratorFrameworkFactory.builder().connectString(this.zkAddress)
                .sessionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
    }

    public void addNodeWatcher(String path,PathChildrenCacheListener listener){
        try {
            PathChildrenCache watcher = new PathChildrenCache(client,basepath+"/"+path,true);
            watcher.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
            watcher.getListenable().addListener(listener);
        } catch (Exception e) {
            LOGGER.error("添加节点监听者失败",e);
        }
    }

    public List<String> getChildren(String key) {
        try {
            return client.getChildren().forPath(basepath+"/"+key);
        } catch (Exception e) {
            LOGGER.error("调用Zookeeper服务失败,请求key为"+key);
        }
        return null;
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
}
