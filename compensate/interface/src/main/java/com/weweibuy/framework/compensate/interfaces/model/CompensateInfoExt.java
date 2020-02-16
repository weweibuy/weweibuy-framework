package com.weweibuy.framework.compensate.interfaces.model;

import lombok.Data;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;

/**
 * @author durenhao
 * @date 2020/2/15 18:09
 **/
@Data
public class CompensateInfoExt extends CompensateInfo {

    private static final BeanCopier COPIER = BeanCopier.create(CompensateInfo.class, CompensateInfoExt.class, false);

    private String id;

    private Integer type;

    private Integer retryCount;

    private Integer alarmCount;

    private LocalDateTime updateTime;

    private String alarmRule;

    private String retryRule;

    public CompensateInfoExt() {
    }

    public CompensateInfoExt(String id, CompensateInfo compensateInfo, CompensateConfigProperties configProperties) {
        this.id = id;
        this.type = configProperties.getCompensateType();
        this.retryCount = 0;
        this.alarmCount = 0;
        this.alarmRule = configProperties.getAlarmRule();
        this.retryRule = configProperties.getRetryRule();
        this.updateTime = LocalDateTime.now();
        COPIER.copy(compensateInfo, this, null);
    }


    public CompensateInfoExt addRetryToCompensateInfo(LocalDateTime nextTriggerTime) {
        retryCount += 1;
        setNextTriggerTime(nextTriggerTime);
        return this;
    }


    public CompensateInfoExt addAlarmToCompensateInfo(LocalDateTime nextTriggerTime) {
        alarmCount += 1;
        setNextTriggerTime(nextTriggerTime);
        return this;
    }

    /**
     * 补偿状态
     */
    public enum CompensateStatus {
        /**
         * 可以重试
         */
        RETRY_ABLE,

        /**
         * 可以报警
         */
        ALARM_ABLE,

        /**
         * 超出报警次数
         */
        OVER_ALARM_COUNT,

    }

}
