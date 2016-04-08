package com.dmall.managed.core.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zoupeng on 16/2/26.
 */
public class Operation implements Serializable {
    private Service service;

    private String qualifier;

    private String name;

    private String displayName;

    private String type;

    private List<Parameter> params = new ArrayList<Parameter>();

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<Parameter> getParams() {
        return params;
    }

    public void setParams(List<Parameter> params) {
        this.params = params;
    }

    public void add(Parameter parameter){
        params.add(parameter);
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
