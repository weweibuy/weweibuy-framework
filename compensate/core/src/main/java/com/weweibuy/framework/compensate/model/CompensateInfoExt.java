package com.weweibuy.framework.compensate.model;

import lombok.Data;
import lombok.ToString;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;

/**
 * 补偿扩展信息
 *
 * @author durenhao
 * @date 2020/2/15 18:09
 **/
@Data
@ToString(callSuper = true)
public class CompensateInfoExt extends CompensateInfo {

    private static final BeanCopier COPIER = BeanCopier.create(CompensateInfo.class, CompensateInfoExt.class, false);

    /**
     * id 号
     */
    private String id;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 报警次数
     */
    private Integer alarmCount;

    private LocalDateTime updateTime;

    /**
     * 报警规则
     */
    private String alarmRule;

    /**
     * 重试规则
     */
    private String retryRule;

    public CompensateInfoExt() {
    }

    public CompensateInfoExt(String id, CompensateInfo compensateInfo, CompensateConfigProperties configProperties) {
        this.id = id;
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

}
