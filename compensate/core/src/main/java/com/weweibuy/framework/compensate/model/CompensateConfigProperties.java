package com.weweibuy.framework.compensate.model;

import lombok.Data;


/**
 * 补偿配置
 *
 * @author durenhao
 * @date 2020/2/13 20:43
 **/
@Data
public class CompensateConfigProperties {

    /**
     * 补偿类型  {@link BuiltInCompensateType}
     */
    private String compensateType;

    /**
     * 重试规则
     */
    private String retryRule;

    /**
     * 报警规则
     */
    private String alarmRule;

}
