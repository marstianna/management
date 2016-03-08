package com.dmall.management.core.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zoupeng on 16/2/26.
 */
public class Operation {
    private Service service;

    private String qualifier;

    private String name;

    private String displayName;

    private Result result;

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

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
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
}
