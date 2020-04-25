package com.weweibuy.framework.compensate.mybatis.po;

import java.time.LocalDateTime;

public class CompensateMethodArgsExt {
    private Long id;

    private Long compensateId;

    private String methodArgs;

    private Integer argsOrder;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompensateId() {
        return compensateId;
    }

    public void setCompensateId(Long compensateId) {
        this.compensateId = compensateId;
    }

    public String getMethodArgs() {
        return methodArgs;
    }

    public void setMethodArgs(String methodArgs) {
        this.methodArgs = methodArgs == null ? null : methodArgs.trim();
    }

    public Integer getArgsOrder() {
        return argsOrder;
    }

    public void setArgsOrder(Integer argsOrder) {
        this.argsOrder = argsOrder;
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