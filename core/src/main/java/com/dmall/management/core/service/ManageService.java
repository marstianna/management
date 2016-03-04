package com.dmall.management.core.service;

import com.dmall.management.core.NodeCache;
import com.dmall.management.core.bean.Node;

/**
 * Created by zoupeng on 16/3/4.
 */
public interface ManageService {
    void manualRegister(String ip,Integer port, String path);

    String nodeQualifierBuilder(Node node);

    void flushNodeInCluster(String ip,Integer port,String path);

    void setNodeCache(NodeCache nodeCache);
}
