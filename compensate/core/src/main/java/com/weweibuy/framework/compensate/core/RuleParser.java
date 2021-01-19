package com.weweibuy.framework.compensate.core;

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

    private static final String UN_LIMIT = "...";

    public static long parser(Integer count, String rule) {
        String[] spiltRule = spiltRule(rule);
        if (spiltRule.length < count + 1) {
            if (UN_LIMIT.equals(spiltRule[spiltRule.length - 1])) {
                if (spiltRule.length > 1) {
                    String s = spiltRule[spiltRule.length - 2];
                    return parserTime(s);
                } else {
                    return 0;
                }
            }
            return -1;
        }
        String current = spiltRule[count];
        if (UN_LIMIT.equals(current) && spiltRule.length > 1) {
            return parserTime(spiltRule[spiltRule.length - 2]);
        }
        return parserTime(current);
    }


    private static String[] spiltRule(String rule) {
        return rule.split(" ");
    }

    private static long parserTime(String rule) {
        if (rule.length() < 2) {
            return 0;
        }
        String unit = rule.substring(rule.length() - 1);
        String num = rule.substring(0, rule.length() - 1);
        if (!StringUtils.isNumeric(num)) {
            return 0;
        }
        switch (unit) {
            case "s":
                return DateTimeUtils.toMils(Long.valueOf(num), TimeUnit.SECONDS);
            case "H":
                return DateTimeUtils.toMils(Long.valueOf(num), TimeUnit.HOURS);
            case "m":
                return DateTimeUtils.toMils(Long.valueOf(num), TimeUnit.MINUTES);
            case "d":
                return DateTimeUtils.toMils(Long.valueOf(num), TimeUnit.DAYS);
            case "M":
                return DateTimeUtils.toMils(Long.valueOf(num), TimeUnit.DAYS) * 30;
            case "y":
                return DateTimeUtils.toMils(Long.valueOf(num), TimeUnit.DAYS) * 365;
            default:
                return 0;
        }
    }


    static CompensateStatus parserToStatus(CompensateInfoExt compensateInfo) {
        long retryTime = parser(compensateInfo.getRetryCount(), compensateInfo.getRetryRule());
        if (retryTime == -1) {
            long alarmTime = parser(compensateInfo.getAlarmCount(), compensateInfo.getAlarmRule());
            if (alarmTime == -1) {
                return CompensateStatus.OVER_ALARM_COUNT;
            }
            return CompensateStatus.ALARM_ABLE;
        }
        return CompensateStatus.RETRY_ABLE;
    }


    static LocalDateTime nextTriggerTime(CompensateInfoExt compensateInf) {
        long time = RuleParser.parser(compensateInf.getRetryCount(), compensateInf.getRetryRule());
        if (time == -1) {
            time = RuleParser.parser(compensateInf.getAlarmCount(), compensateInf.getAlarmRule());
        }
        // 计算下次触发时间
        return LocalDateTime.now().plus(time, ChronoUnit.MILLIS);
    }
}
