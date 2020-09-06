package com.weweibuy.framework.compensate.model;

import lombok.Getter;

/**
 * 补偿结果枚举
 *
 * @author durenhao
 * @date 2020/5/31 20:23
 **/
@Getter
public enum CompensateResultEum {


    /**
     * 补偿重试失败
     */
    RETRY_FAIL,

    /**
     * 补偿重试失败,触发恢复方法
     */
    RETRY_FAIL_RECOVER,

    /**
     * 补偿重试成功
     */
    RETRY_SUCCESS,

    /**
     * 报警
     */
    ALARM,

    /**
     * 超出报警上限 补偿失败
     */
    OVER_ALARM_COUNT,;


}
