package com.weweibuy.framework.compensate.model;

/**
 * @author durenhao
 * @date 2020/9/6 19:47
 **/
public enum  CompensateStatus {

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
