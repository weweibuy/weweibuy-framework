package com.weweibuy.framework.common.core.concurrent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CustomizableThreadCreator;

import java.util.concurrent.ThreadFactory;

/**
 * 为线程增加为捕获异常处理器的线程工厂
 *
 * @author durenhao
 * @date 2020/9/12 21:35
 **/
@Slf4j
public class LogExceptionThreadFactory extends CustomizableThreadCreator implements ThreadFactory {

    private static final LogUncaughtExceptionHandler LOG_UNCAUGHT_EXCEPTION_HANDLER = new LogUncaughtExceptionHandler();

    public LogExceptionThreadFactory() {
        super();
    }

    public LogExceptionThreadFactory(String threadNamePrefix) {
        super(threadNamePrefix);
    }


    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = createThread(runnable);
        thread.setUncaughtExceptionHandler(LOG_UNCAUGHT_EXCEPTION_HANDLER);
        return thread;
    }
}
