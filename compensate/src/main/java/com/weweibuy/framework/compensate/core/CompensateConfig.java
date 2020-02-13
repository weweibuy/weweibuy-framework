package com.weweibuy.framework.compensate.core;

import lombok.Data;

/**
 * 补偿配置
 *
 * @author durenhao
 * @date 2020/2/13 20:43
 **/
@Data
public class CompensateConfig {

    private String retryRule;

    private String alarmRule;

    private Integer maxRetry;

    private Integer maxAlarm;
}
