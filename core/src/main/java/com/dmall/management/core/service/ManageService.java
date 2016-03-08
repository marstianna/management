package com.dmall.management.core.service;

import com.dmall.management.core.NodeCache;

/**
 * 管理节点的service方法
 * Created by zoupeng on 16/3/4.
 */
public interface ManageService {

    /**
     * 手动注册一个cluster
     * @param ip    cluster的ip
     * @param port  cluster的port
     * @param path  请求的地址
     */
    void manualRegister(String ip,Integer port, String path);

    /**
     * 强制刷新cluster的node缓存(NodeParser中的node)
     * @param ip    cluster的ip
     * @param port  cluster的port
     * @param path  请求地址
     */
    void flushNodeInCluster(String ip,Integer port,String path);

    void setNodeCache(NodeCache nodeCache);
}
