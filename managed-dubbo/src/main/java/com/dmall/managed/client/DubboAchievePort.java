package com.dmall.managed.client;

import com.dmall.managed.core.IAchievePort;

/**
 * Created by zoupeng on 16/4/7.
 */
public class DubboAchievePort implements IAchievePort {
    private Integer port;

    @Override
    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
