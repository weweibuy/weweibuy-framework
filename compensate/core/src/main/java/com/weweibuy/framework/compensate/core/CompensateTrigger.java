package com.weweibuy.framework.compensate.core;

/**
 * 补偿触发器
 *
 * @author durenhao
 * @date 2020/2/13 19:56
 **/
public interface CompensateTrigger {

    /**
     * 触发补偿
     *
     * @param args
     */
    void trigger(Object... args);

}
