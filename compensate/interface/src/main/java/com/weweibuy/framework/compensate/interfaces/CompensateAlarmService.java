package com.weweibuy.framework.compensate.interfaces;


import com.weweibuy.framework.compensate.interfaces.model.CompensateInfoExt;

/**
 * 补偿报警服务
 *
 * @author durenhao
 * @date 2020/2/15 19:33
 **/
public interface CompensateAlarmService {

    /**
     * 发送报警
     *
     * @param compensateInfoExt
     */
    void sendAlarm(CompensateInfoExt compensateInfoExt);

}
