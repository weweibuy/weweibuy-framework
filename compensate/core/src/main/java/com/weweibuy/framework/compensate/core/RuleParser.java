package com.weweibuy.framework.compensate.core;

import com.weweibuy.framework.common.core.support.ExecRuleParser;
import com.weweibuy.framework.common.core.utils.DateTimeUtils;
import com.weweibuy.framework.compensate.model.CompensateInfoExt;
import com.weweibuy.framework.compensate.model.CompensateStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * 规则解析器
 *
 * @author durenhao
 * @date 2020/2/15 18:49
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RuleParser {


    static CompensateStatus parserToStatus(CompensateInfoExt compensateInfo) {
        long retryTime = ExecRuleParser.parser(compensateInfo.getRetryCount(), compensateInfo.getRetryRule());
        if (retryTime == -1) {
            long alarmTime = ExecRuleParser.parser(compensateInfo.getAlarmCount(), compensateInfo.getAlarmRule());
            if (alarmTime == -1) {
                return CompensateStatus.OVER_ALARM_COUNT;
            }
            return CompensateStatus.ALARM_ABLE;
        }
        return CompensateStatus.RETRY_ABLE;
    }


    static LocalDateTime nextTriggerTime(CompensateInfoExt compensateInf) {
        long time = ExecRuleParser.parser(compensateInf.getRetryCount(), compensateInf.getRetryRule());
        if (time == -1) {
            time = ExecRuleParser.parser(compensateInf.getAlarmCount(), compensateInf.getAlarmRule());
        }
        // 计算下次触发时间
        return LocalDateTime.now().plus(time, ChronoUnit.MILLIS);
    }
}
