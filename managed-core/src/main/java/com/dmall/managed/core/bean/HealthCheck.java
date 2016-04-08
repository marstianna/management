package com.dmall.managed.core.bean;

import com.alibaba.fastjson.JSON;
import com.dmall.managed.core.server.service.iface.AutoFixCacheService;
import com.dmall.managed.core.server.service.iface.WarningStrategy;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by zoupeng on 16/3/29.
 */
public class HealthCheck {
    private WarningStrategy warningStrategy;

    private AutoFixCacheService autoFixCacheService;

    private String defaultValue;

    private String targetNodeQualifier;

    private String targetOperationQualifier;

    private String currentValue;

    private String cron;

    private Exception invokeException;

    private boolean isWarning = false;

    public WarningStrategy getWarningStrategy() {
        return warningStrategy;
    }

    public void setWarningStrategy(WarningStrategy warningStrategy) {
        this.warningStrategy = warningStrategy;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public boolean isWarning() {
        return isWarning;
    }

    public void setWarning(boolean warning) {
        isWarning = warning;
    }

    public String getTargetOperationQualifier() {
        return targetOperationQualifier;
    }

    public void setTargetOperationQualifier(String targetOperationQualifier) {
        this.targetOperationQualifier = targetOperationQualifier;
    }

    public Exception getInvokeException() {
        return invokeException;
    }

    public void setInvokeException(Exception invokeException) {
        this.invokeException = invokeException;
    }

    public AutoFixCacheService getAutoFixCacheService() {
        return autoFixCacheService;
    }

    public void setAutoFixCacheService(AutoFixCacheService autoFixCacheService) {
        this.autoFixCacheService = autoFixCacheService;
    }

    public String getTargetNodeQualifier() {
        return targetNodeQualifier;
    }

    public void setTargetNodeQualifier(String targetNodeQualifier) {
        this.targetNodeQualifier = targetNodeQualifier;
    }

    public void selfCheck(Object result){
        String current = (result instanceof String) ? String.valueOf(result) : JSON.toJSONString(result);
        this.setCurrentValue(current);
        if(StringUtils.isBlank(this.currentValue) || !this.defaultValue.equals(this.currentValue)){
            this.tryAutoFix();
            boolean fixSuccess = doubleCheck();
            if(!fixSuccess) {
                this.isWarning();
            }
        }
    }


    public void tryAutoFix(){
        if(autoFixCacheService != null) {
            this.autoFixCacheService.autoFix(this);
        }
    }

    private boolean doubleCheck(){
        return !StringUtils.isBlank(this.currentValue) && this.defaultValue.equals(this.currentValue);
    }

    public void warning(){
        if(this.isWarning) {
            this.warningStrategy.warning(this);
        }
    }
}
