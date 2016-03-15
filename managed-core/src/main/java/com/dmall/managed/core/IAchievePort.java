package com.dmall.managed.core;

/**
 *  当使用默认的从本地jmx获取port失败时,将执行该获取port的策略
 *
 * Created by zoupeng on 16/2/26.
 */
public interface IAchievePort {

    Integer getPort();
}
