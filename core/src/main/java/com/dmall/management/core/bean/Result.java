package com.dmall.management.core.bean;

/**
 * Created by zoupeng on 16/2/26.
 */
public class Result {
    private Operation operation;

    private String desc;
    private String value;
    private String type;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
