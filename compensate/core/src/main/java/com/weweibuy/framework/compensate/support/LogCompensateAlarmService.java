package com.weweibuy.framework.compensate.support;

import com.weweibuy.framework.compensate.core.CompensateAlarmService;
import com.weweibuy.framework.compensate.model.CompensateInfo;
import com.weweibuy.framework.compensate.model.CompensateInfoExt;
import lombok.extern.slf4j.Slf4j;

/**
 * @author durenhao
 * @date 2020/2/15 19:35
 **/
@Slf4j
public class LogCompensateAlarmService implements CompensateAlarmService {

    @Override
    public void sendAlarm(CompensateInfoExt compensateInfoExt) {
        log.warn("补偿信息: {}, 重试超出上限", compensateInfoExt);
    }

    @Override
    public void sendSaveCompensateAlarm(CompensateInfo compensateInfo, Exception e) {
        log.warn("保存补偿信息: {} 时出现异常: ", compensateInfo, e);
    }


}
