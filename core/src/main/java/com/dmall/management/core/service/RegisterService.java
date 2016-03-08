package com.dmall.management.core.service;

import com.dmall.management.core.Resource;
import com.dmall.management.core.parser.NodeParser;

/**
 * cluster的注册服务
 *
 * Created by zoupeng on 16/3/4.
 */
public interface RegisterService {
    /**
     * cluster向master节点注册
     */
    void register();

    void setNodeParser(NodeParser nodeParser);

    void setResource(Resource resource);
}
