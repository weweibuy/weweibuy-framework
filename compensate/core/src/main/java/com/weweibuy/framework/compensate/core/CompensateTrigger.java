package com.weweibuy.framework.compensate.core;


import com.weweibuy.framework.compensate.model.CompensateResult;

import java.util.List;
import java.util.Set;

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
    List<CompensateResult> trigger(Object... args);

    /**
     * 强制触发
     *
     * @param idSet
     */
    List<CompensateResult> forceTrigger(Set<String> idSet);


}
