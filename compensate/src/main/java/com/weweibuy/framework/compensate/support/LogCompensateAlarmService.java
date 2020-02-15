package com.weweibuy.framework.compensate.support;

import com.weweibuy.framework.compensate.core.CompensateInfoExt;
import lombok.extern.slf4j.Slf4j;

/**
 * @author durenhao
 * @date 2020/2/15 19:35
 **/
@Slf4j
public class LogCompensateAlarmService implements CompensateAlarmService {

    @Override
    public void sendAlarm(CompensateInfoExt compensateInfoExt) {
        log.info("补偿信息: {}, 重试超出上限", compensateInfoExt);
    }
}
