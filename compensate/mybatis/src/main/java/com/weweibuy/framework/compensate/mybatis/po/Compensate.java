package com.weweibuy.framework.compensate.mybatis.po;

import java.time.LocalDateTime;

public class Compensate {
    private Long id;

    private String compensateKey;

    private String bizId;

    private String compensateType;

    private String methodArgs;

    private LocalDateTime nextTriggerTime;

    private Integer retryCount;

    private Integer alarmCount;

    private Boolean hasArgsExt;

    private Boolean isDelete;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompensateKey() {
        return compensateKey;
    }

    public void setCompensateKey(String compensateKey) {
        this.compensateKey = compensateKey == null ? null : compensateKey.trim();
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId == null ? null : bizId.trim();
    }

    public String getCompensateType() {
        return compensateType;
    }

    public void setCompensateType(String compensateType) {
        this.compensateType = compensateType == null ? null : compensateType.trim();
    }

    public String getMethodArgs() {
        return methodArgs;
    }

    public void setMethodArgs(String methodArgs) {
        this.methodArgs = methodArgs == null ? null : methodArgs.trim();
    }

    public LocalDateTime getNextTriggerTime() {
        return nextTriggerTime;
    }

    public void setNextTriggerTime(LocalDateTime nextTriggerTime) {
        this.nextTriggerTime = nextTriggerTime;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getAlarmCount() {
        return alarmCount;
    }

    public void setAlarmCount(Integer alarmCount) {
        this.alarmCount = alarmCount;
    }

    public Boolean getHasArgsExt() {
        return hasArgsExt;
    }

    public void setHasArgsExt(Boolean hasArgsExt) {
        this.hasArgsExt = hasArgsExt;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}