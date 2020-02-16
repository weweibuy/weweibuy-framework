package com.weweibuy.framework.compensate.core;

import com.weweibuy.framework.compensate.support.RuleParser;
import lombok.Data;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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


    public CompensateStatus parserCompensateStatus() {
        long retryTime = RuleParser.parser(getRetryCount(), retryRule);
        if (retryTime == -1) {
            long alarmTime = RuleParser.parser(getAlarmCount(), alarmRule);
            if (alarmTime == -1) {
                return CompensateStatus.OVER_ALARM_COUNT;
            }
            return CompensateStatus.ALARM_ABLE;
        }
        return CompensateStatus.RETRY_ABLE;
    }


    public CompensateInfoExt addRetryToCompensateInfo() {
        retryCount += 1;
        long time = RuleParser.parser(getRetryCount(), retryRule);
        if (time == -1) {
            time = RuleParser.parser(getAlarmCount(), alarmRule);
        }
        // 计算下次触发时间
        setNextTriggerTime(LocalDateTime.now().plus(time, ChronoUnit.MILLIS));
        return this;
    }


    public CompensateInfoExt addAlarmToCompensateInfo() {
        alarmCount += 1;
        long time = RuleParser.parser(getAlarmCount(), alarmRule);
        // 计算下次触发时间
        setNextTriggerTime(LocalDateTime.now().plus(time, ChronoUnit.MILLIS));
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
