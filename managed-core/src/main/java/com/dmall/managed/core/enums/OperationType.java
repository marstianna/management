package com.dmall.managed.core.enums;

/**
 * Created by zoupeng on 16/3/10.
 */
public enum OperationType {
    /**
     * 服务只能用于中央节点广播所有已注册该服务的节点
     */
    BROADCAST("broadcast"),
    /**
     * 服务只能用于中央节点按照注册节点的ip通知节点执行该方法
     */
    SINGLE("single"),
    /**
     * ALL = BROADCAST+SINGLE,即即可广播,也可单对单
     */
    ALL("all"),
    /**
     * 该类型用于healthCheck
     */
    HEALTH_CHECK("health_chekc");

    String name;

    OperationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
