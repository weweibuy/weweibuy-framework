package com.weweibuy.framework.compensate.core;


import com.weweibuy.framework.compensate.model.CompensateInfo;
import com.weweibuy.framework.compensate.model.CompensateInfoExt;

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

    /**
     * 保存补偿信息是异常处理
     *
     * @param compensateInfo
     * @param e
     */
    void sendSaveCompensateAlarm(CompensateInfo compensateInfo, Exception e);


}
