package com.weweibuy.framework.compensate.support;

import com.weweibuy.framework.compensate.model.CompensateResult;
import com.weweibuy.framework.compensate.model.CompensateStatus;

/**
 * @author durenhao
 * @date 2020/9/6 19:25
 **/
public interface CompensateRecorder {

    /**
     * 记录补偿信息
     *
     * @param compensateResult
     * @param force
     * @param compensateStatus
     */
    void recorderCompensate(CompensateResult compensateResult, Boolean force, CompensateStatus compensateStatus);

}
