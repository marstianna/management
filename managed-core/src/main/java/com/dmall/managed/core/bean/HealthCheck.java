package com.dmall.managed.core.bean;

import com.alibaba.fastjson.JSON;
import com.dmall.managed.core.server.service.iface.AutoFixCacheService;
import com.dmall.managed.core.server.service.iface.WarningStrategy;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by zoupeng on 16/3/29.
 */
public class HealthCheck {
    //报警策略,如果出现defaultValue和currentValue不一致,且isWarning = true时,会触发warningStrategy.warning();
    private WarningStrategy warningStrategy;

    //自动修复,如果出现defaultValue和currentValue不一致,会自动调用修复方法
    private AutoFixCacheService autoFixCacheService;

    //期待节点放回的值
    private String defaultValue;

    //目标节点的qualifier
    private String targetNodeQualifier;

    //指定的healthCheck方法
    private String targetOperationQualifier;

    //节点返回的值
    private String currentValue;

    //cron表达式,用于worker调用设置
    private String cron;

    //调用后触发的异常
    private Exception invokeException;

    //是否报警
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
