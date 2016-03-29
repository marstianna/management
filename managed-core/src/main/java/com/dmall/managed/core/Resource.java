package com.dmall.managed.core;

/**
 * Created by zoupeng on 16/3/4.
 */
public interface Resource {

    String get(String key);
    /**
     * 返回受影响行数
     * @param key
     * @param value
     * @return
     */
    Integer update(String key,String value);
}
