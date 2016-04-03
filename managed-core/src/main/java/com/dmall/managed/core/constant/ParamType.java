package com.dmall.managed.core.constant;

/**
 * * ManagementParam的参数类型
 * Created by zoupeng on 16/4/3.
 */
public enum ParamType {
    /**
     * 用于展示在界面输入
     */
    PARAM_SHOW("show"),
    /**
     * 用于方法入参
     */
    PARAM_INPUT("input");

    String name;

    ParamType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
