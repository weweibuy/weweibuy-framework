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

    private String alarmRule;

    private String retryRule;

    public CompensateInfoExt(String id, CompensateInfo compensateInfo, CompensateConfigProperties configProperties) {
        this.id = id;
        this.type = configProperties.getCompensateType();
        this.alarmRule = configProperties.getAlarmRule();
        this.retryRule = configProperties.getRetryRule();
        COPIER.copy(compensateInfo, this, null);
    }

    /**
     * 是否应该补偿
     *
     * @return
     */
    public boolean shouldCompensate() {
        long parser = RuleParser.parser(getRetryCount(), retryRule);
        if (parser == -1) {
            return false;
        }
        return System.currentTimeMillis() - DateUtils.localDateTimeToTimestamp(getUpdateTime()) > parser;
    }

    /**
     * 是否应该报警
     *
     * @return
     */
    public boolean shouldAlarm() {
        long parser = RuleParser.parser(getAlarmCount(), alarmRule);
        if (parser == -1) {
            return false;
        }
        return System.currentTimeMillis() - DateUtils.localDateTimeToTimestamp(getUpdateTime()) > parser;

    }


    public CompensateInfo addRetryToCompensateInfo() {
        CompensateInfo compensateInfo = new CompensateInfo();
        compensateInfo.setArgs(getArgs());
        compensateInfo.setCompensateKey(getCompensateKey());
        compensateInfo.setBizId(getBizId());
        compensateInfo.setRetryCount(getRetryCount() + 1);
        compensateInfo.setAlarmCount(getAlarmCount());
        compensateInfo.setUpdateTime(LocalDateTime.now());
        return compensateInfo;
    }

    public CompensateInfo addAlarmToCompensateInfo() {
        CompensateInfo compensateInfo = new CompensateInfo();
        compensateInfo.setArgs(getArgs());
        compensateInfo.setCompensateKey(getCompensateKey());
        compensateInfo.setBizId(getBizId());
        compensateInfo.setRetryCount(getRetryCount());
        compensateInfo.setAlarmCount(getAlarmCount() + 1);
        compensateInfo.setUpdateTime(LocalDateTime.now());
        return compensateInfo;
    }


}
