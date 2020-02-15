package com.weweibuy.framework.compensate.core;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author durenhao
 * @date 2020/2/13 22:19
 **/
@Slf4j
public class SimpleCompensateTrigger implements CompensateTrigger {

    private CompensateStore compensateStore;

    private CompensateHandlerService compensateHandlerService;

    private final Timer connectionManagerTimer = new Timer(
            "com.weweibuy.framework.compensate.core.SimpleCompensateTrigger", true);

    public SimpleCompensateTrigger(CompensateStore compensateStore, CompensateHandlerService compensateHandlerService) {
        this.compensateStore = compensateStore;
        this.compensateHandlerService = compensateHandlerService;
        init();
    }

    public void init() {

        this.connectionManagerTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                trigger();
            }
        }, 5000, 3000);
    }

    @Override
    public void trigger(Object... args) {
        // 触发补偿
        try {
            Collection<CompensateInfoExt> compensateInfoCollection = compensateStore.queryCompensateInfo();
            compensateHandlerService.compensate(compensateInfoCollection);
        } catch (Exception e) {
            log.warn("补偿是发生异常: {}", e);
        }
    }
}
