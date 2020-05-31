package com.weweibuy.framework.samples.compensate.config;

import com.weweibuy.framework.compensate.config.CompensateConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;

/**
 * @author durenhao
 * @date 2020/2/17 21:24
 **/
@Configuration
public class ThreadPoolCompensateConfigurer implements CompensateConfigurer {

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Override
    public ExecutorService getAdviceExecutorService() {
        return taskExecutor.getThreadPoolExecutor();
    }

    @Override
    public ExecutorService getCompensateExecutorService() {
        return taskExecutor.getThreadPoolExecutor();
    }
}
