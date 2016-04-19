package com.dmall.managed.core;

/**
 * @see com.dmall.managed.core.client.ManagementConfig
 * 如果Cluster节点配置ManagementConfig.achievePort
 * 那么将会从配置的这个IAchievePort获取提供服务的端口
 * 否则,将自动获取该服务启动tomcat时,自动注册的mbean
 *
 *
 * Created by zoupeng on 16/2/26.
 */
public interface IAchievePort {

    Integer getPort();
}
