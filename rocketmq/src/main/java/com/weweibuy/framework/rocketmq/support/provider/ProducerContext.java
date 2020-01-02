package com.weweibuy.framework.rocketmq.support.provider;

import lombok.Data;

/**
 * @author durenhao
 * @date 2020/1/2 17:43
 **/
@Data
public class ProducerContext {

    /**
     * 总计处理器数量
     */
    private Integer processorTotal;

    /**
     * 当前位置
     */
    private Integer pos;

}
