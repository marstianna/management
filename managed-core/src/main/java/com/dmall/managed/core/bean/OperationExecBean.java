package com.dmall.managed.core.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zoupeng on 16/3/29.
 */
public class OperationExecBean {
    private String nodeQualifier;
    private String operationQualifier;
    private Map<String, Object> params = new HashMap<>();

    public String getNodeQualifier() {
        return nodeQualifier;
    }

    public void setNodeQualifier(String nodeQualifier) {
        this.nodeQualifier = nodeQualifier;
    }

    public String getOperationQualifier() {
        return operationQualifier;
    }

    public void setOperationQualifier(String operationQualifier) {
        this.operationQualifier = operationQualifier;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
