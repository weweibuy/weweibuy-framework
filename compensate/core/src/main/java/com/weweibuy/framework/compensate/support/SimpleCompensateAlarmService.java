package com.weweibuy.framework.compensate.support;

import com.weweibuy.framework.common.core.support.AlarmService;
import com.weweibuy.framework.compensate.core.CompensateAlarmService;
import com.weweibuy.framework.compensate.model.CompensateInfo;
import com.weweibuy.framework.compensate.model.CompensateInfoExt;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @author durenhao
 * @date 2020/2/15 19:35
 **/
@Slf4j
public class SimpleCompensateAlarmService implements CompensateAlarmService {

    private static final String ALARM_BIZ_TYPE = "common_compensate_alarm_type";

    private AlarmService alarmService;

    public SimpleCompensateAlarmService(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    @Override
    public void sendAlarm(CompensateInfoExt compensateInfoExt) {
        alarmService.sendAlarm(AlarmService.AlarmLevel.WARN, ALARM_BIZ_TYPE, buildMsg0(compensateInfoExt));
    }

    @Override
    public void sendSaveCompensateAlarm(CompensateInfo compensateInfo, Exception e) {
        log.warn("保存补偿信息: 业务号: {}, 补偿key: {}, 补偿方式: {}, 补偿数据:{}, 异常: ",
                compensateInfo.getBizId(),
                compensateInfo.getCompensateKey(),
                compensateInfo.getCompensateType(),
                compensateInfo.getMethodArgs(),
                e);
        alarmService.sendAlarm(AlarmService.AlarmLevel.WARN, ALARM_BIZ_TYPE, buildMsg1(compensateInfo, e));
    }

    @Override
    public void sendRecoverAlarm(CompensateInfoExt compensateInfoExt, Throwable e) {
        alarmService.sendAlarm(AlarmService.AlarmLevel.WARN, ALARM_BIZ_TYPE, buildMsg2(compensateInfoExt, e));
    }


    private String buildMsg0(CompensateInfoExt compensateInfoExt) {
        String msgTem = "【补偿超出重试上限】补偿id: %s, 业务号: %s, 补偿key: %s, 补偿方式: %s, 目前已经重试: %s 次, 已到达重试上限, 请手动处理.";
        return String.format(msgTem, compensateInfoExt.getId(),
                Optional.ofNullable(compensateInfoExt.getBizId()).orElse("-"),
                compensateInfoExt.getCompensateKey(),
                compensateInfoExt.getCompensateType(),
                compensateInfoExt.getRetryCount());
    }

    private String buildMsg1(CompensateInfo compensateInfo, Exception e) {
        String msgTem = "【补偿信息保存异常】 业务号: %s, 补偿key: %s, 补偿方式: %s, 补偿数据: %s, 保存异常: %s";
        return String.format(msgTem,
                Optional.ofNullable(compensateInfo.getBizId()).orElse("-"),
                compensateInfo.getCompensateKey(),
                compensateInfo.getCompensateType(),
                Optional.ofNullable(compensateInfo.getMethodArgs()).orElse("-"),
                e.getMessage());
    }

    private String buildMsg2(CompensateInfoExt compensateInfoExt, Throwable e) {
        String msgTem = "【补偿执行恢复方法异常】补偿id: %s, 业务号: %s, 补偿key: %s, 补偿方式: %s, 执行恢复方法异常: %s";

        return String.format(msgTem,
                compensateInfoExt.getId(),
                Optional.ofNullable(compensateInfoExt.getBizId()).orElse("-"),
                compensateInfoExt.getCompensateKey(),
                compensateInfoExt.getCompensateType(),
                e.getMessage());
    }

}
