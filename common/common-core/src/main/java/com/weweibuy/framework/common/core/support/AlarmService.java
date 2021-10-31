package com.weweibuy.framework.common.core.support;

/**
 * 报警服务
 *
 * @author durenhao
 * @date 2020/12/30 21:15
 **/
public interface AlarmService {

    /**
     * 发送报警
     *
     * @param bizType 业务类型
     * @param msg     报警消息
     */
    void sendAlarm(String bizType, String msg);


    /**
     * 发送报警
     *
     * @param bizType
     * @param msgFormat
     * @param msg
     */
    default void sendAlarmFormatMsg(String bizType, String msgFormat, String... msg) {
        sendAlarm(bizType, String.format(msgFormat, msg));
    }

}
