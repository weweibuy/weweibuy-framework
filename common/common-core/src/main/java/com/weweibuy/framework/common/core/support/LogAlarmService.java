package com.weweibuy.framework.common.core.support;

import lombok.extern.slf4j.Slf4j;

/**
 * 日志报警
 *
 * @author durenhao
 * @date 2020/12/30 21:20
 **/
@Slf4j
public class LogAlarmService implements AlarmService {

    @Override
    public void sendAlarm(AlarmLevel alarmLevel, String bizType, String msg) {
        log.warn("业务报警: 级别: {}, 业务类型: {}, 报警内容: {}", alarmLevel, bizType, msg);
    }
}
