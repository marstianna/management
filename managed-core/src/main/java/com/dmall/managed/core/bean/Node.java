package com.dmall.managed.core.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zoupeng on 16/2/26.
 */
public class Node implements Serializable{
    private String ip;
    private Integer port;
    private String desc;
    private String name;
    private String group;
    private String displayName;
    private String nodeQualifier;
    private String basePath;

    private List<Service> services = new ArrayList<Service>();

    @JSONField(serialize = false,deserialize = false)
    public List<Operation> allOperationsInNode(){
        List<Operation> operations = new ArrayList<>();
        for(Service service : services){
            operations.addAll(service.getOperations());
        }
        return operations;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public void add(Service service){
        this.services.add(service);
    }

    public String getNodeQualifier() {
        return nodeQualifier;
    }

    public void setNodeQualifier(String nodeQualifier) {
        this.nodeQualifier = nodeQualifier;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
