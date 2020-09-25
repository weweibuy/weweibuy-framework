package com.weweibuy.framework.compensate.core;

import com.weweibuy.framework.common.core.concurrent.LogExceptionThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author durenhao
 * @date 2020/2/13 22:19
 **/
@Slf4j
public class SimpleTimerCompensateTrigger extends AbstractCompensateTrigger implements SmartInitializingSingleton {

    private static final ScheduledExecutorService SCHEDULE = new ScheduledThreadPoolExecutor(1,
            new LogExceptionThreadFactory("compensate-schedule-"),
            new ThreadPoolExecutor.DiscardPolicy());

    private void trigger0() {
        try {
            trigger();
        } catch (Exception e) {
            log.warn("补偿时发生异常: ", e);
        }
    }

    @Override
    public void afterSingletonsInstantiated() {
        SCHEDULE.scheduleAtFixedRate(this::trigger0, 5, 10, TimeUnit.SECONDS);
    }
}
