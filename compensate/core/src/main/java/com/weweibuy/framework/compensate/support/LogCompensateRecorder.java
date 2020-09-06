package com.weweibuy.framework.compensate.support;

import com.weweibuy.framework.compensate.model.CompensateResult;
import com.weweibuy.framework.compensate.model.CompensateStatus;
import lombok.extern.slf4j.Slf4j;

/**
 * @author durenhao
 * @date 2020/9/6 19:32
 **/
@Slf4j
public class LogCompensateRecorder implements CompensateRecorder {

    @Override
    public void recorderCompensate(CompensateResult compensateResult, Boolean force, CompensateStatus compensateStatus) {
        log.info("补偿id: {}, 结果: {}", compensateResult.getId(), compensateResult.getResult());
    }
}
