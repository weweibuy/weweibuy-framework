package com.weweibuy.framework.common.core.concurrent;

import com.weweibuy.framework.common.core.exception.Exceptions;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class LogExceptionThreadFactoryTest {

 private ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 30, 60L,TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(), new LogExceptionThreadFactory("test-thread-"),
                new ThreadPoolExecutor.CallerRunsPolicy());


    @Test
    public void newThread() throws Exception {
        executor.execute(() ->  {
            log.info("线程执行任务中....");
            throw Exceptions.unknown();
        });
        Thread.sleep(2000);
    }

}