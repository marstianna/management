package com.dmall.management.core.service;

import com.dmall.management.core.Resource;
import com.dmall.management.core.parser.NodeParser;

/**
 * Created by zoupeng on 16/3/4.
 */
public interface RegisterService {
    void register(String path);

    void setNodeParser(NodeParser nodeParser);

    void setResource(Resource resource);
}
