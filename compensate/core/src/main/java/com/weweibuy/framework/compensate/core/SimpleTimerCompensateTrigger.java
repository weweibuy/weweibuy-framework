package com.weweibuy.framework.compensate.core;

import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author durenhao
 * @date 2020/2/13 22:19
 **/
@Slf4j
public class SimpleTimerCompensateTrigger extends AbstractCompensateTrigger {


    private final Timer connectionManagerTimer = new Timer(
            "SimpleTimerCompensateTrigger", true);

    public SimpleTimerCompensateTrigger() {
        init();
    }

    public void init() {
        this.connectionManagerTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                trigger0();
            }
        }, 5000, 3000);
    }


    private void trigger0() {
        try {
            trigger();
        } catch (Exception e) {
            log.warn("补偿时发生异常: ", e);
        }
    }

}
