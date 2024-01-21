package com.weweibuy.framework.biztask.support;

import com.weweibuy.framework.biztask.core.AbstractBizTaskTrigger;
import com.weweibuy.framework.biztask.core.BizTaskExecutor;
import com.weweibuy.framework.biztask.db.repository.BizTaskRepository;
import com.weweibuy.framework.common.core.concurrent.LogExceptionThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author durenhao
 * @date 2024/1/20 14:06
 **/
@Slf4j
public class SimpleBizTaskTrigger extends AbstractBizTaskTrigger implements SmartInitializingSingleton {

    private static final ScheduledExecutorService SCHEDULE = new ScheduledThreadPoolExecutor(1,
            new LogExceptionThreadFactory("biz-task-"),
            new ThreadPoolExecutor.DiscardPolicy());

    public SimpleBizTaskTrigger(BizTaskRepository bizTaskRepository, BizTaskExecutor bizTaskExecutor) {
        super(bizTaskRepository, bizTaskExecutor);
    }

    @Override
    public void afterSingletonsInstantiated() {
        SCHEDULE.scheduleAtFixedRate(this::trigger0, 5, 10, TimeUnit.SECONDS);
    }

    private void trigger0() {
        try {
            trigger(null, null, null);
        } catch (Exception e) {
            log.error("执行异常: ", e);
        }
    }

}
