package com.weweibuy.framework.common.core.support;

/**
 * 报警服务
 *
 * @author durenhao
 * @date 2020/12/30 21:15
 **/
public interface AlarmService {

    /**
     * 报警级别
     */
    public enum AlarmLevel {

        /**
         * 紧急
         */
        CRITICAL,

        /**
         * 警告
         */
        WARN,

        /**
         * 信息
         */
        INFO,;


    }

    /**
     * 发送报警
     *
     * @param alarmLevel 级别
     * @param bizType    业务类型
     * @param msg        报警消息
     */
    void sendAlarm(AlarmLevel alarmLevel, String bizType, String msg);


    /**
     * 发送报警
     *
     * @param alarmLevel 级别
     * @param bizType
     * @param msgFormat
     * @param msg
     */
    default void sendAlarmFormatMsg(AlarmLevel alarmLevel, String bizType, String msgFormat, Object... msg) {
        sendAlarm(alarmLevel, bizType, String.format(msgFormat, msg));
    }

}
