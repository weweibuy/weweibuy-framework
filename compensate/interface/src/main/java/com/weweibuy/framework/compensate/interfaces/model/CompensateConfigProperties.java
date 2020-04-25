package com.weweibuy.framework.compensate.interfaces.model;

import lombok.Data;

/**
 * 补偿配置
 *
 * @author durenhao
 * @date 2020/2/13 20:43
 **/
@Data
public class CompensateConfigProperties {

    private String compensateType;

    private String retryRule;

    private String alarmRule;

}
