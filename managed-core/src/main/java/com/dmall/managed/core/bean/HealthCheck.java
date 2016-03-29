package com.dmall.managed.core.bean;

/**
 * Created by zoupeng on 16/3/29.
 */
public class HealthCheck {
    private WarningStrategy warningStrategy;

    private String defaultValue;

    private String targetOperationQualifier;

    private String currentValue;

    private String cron;

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
}
