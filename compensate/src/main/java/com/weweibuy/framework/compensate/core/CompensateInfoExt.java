package com.weweibuy.framework.compensate.core;

import com.weweibuy.framework.compensate.support.RuleParser;
import com.weweibuy.framework.compensate.utils.DateUtils;
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


    public CompensateStatus parserCompensateStatus() {
        long retryTime = RuleParser.parser(getRetryCount(), retryRule);
        if (retryTime == -1) {
            long alarmTime = RuleParser.parser(getAlarmCount(), alarmRule);
            if (alarmTime == -1) {
                return CompensateStatus.OVER_ALARM_COUNT;
            }
            if (DateUtils.isCurrentTimeOverInterval(getUpdateTime(), alarmTime)) {
                return CompensateStatus.ALARM_ABLE;
            }
            return CompensateStatus.NOT_IN_ALARM_TIME;
        } else if (DateUtils.isCurrentTimeOverInterval(getUpdateTime(), retryTime)) {
            return CompensateStatus.RETRY_ABLE;
        } else {
            return CompensateStatus.NOT_IN_RETRY_TIME;
        }
    }


    public CompensateInfoExt addRetryToCompensateInfo() {
        retryCount += 1;
        return this;
    }

    public CompensateInfoExt addAlarmToCompensateInfo() {
        alarmCount += 1;
        return this;
    }

    /**
     * 补偿状态
     */
    public static enum CompensateStatus {
        /**
         * 可以重试
         */
        RETRY_ABLE,

        /**
         * 不在重试时间内
         */
        NOT_IN_RETRY_TIME,

        /**
         * 可以报警
         */
        ALARM_ABLE,

        /**
         * 不在报警时间内
         */
        NOT_IN_ALARM_TIME,

        /**
         * 超出报警次数
         */
        OVER_ALARM_COUNT,

    }

}
