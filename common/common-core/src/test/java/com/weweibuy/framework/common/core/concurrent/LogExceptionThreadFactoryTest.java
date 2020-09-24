package com.weweibuy.framework.common.core.concurrent;

import com.weweibuy.framework.common.core.exception.Exceptions;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.*;

@Slf4j
public class LogExceptionThreadFactoryTest {

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 30, 60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(), new LogExceptionThreadFactory("test-thread-"),
            new ThreadPoolExecutor.CallerRunsPolicy());


    @Test
    public void newThread() throws Exception {
        executor.execute(() -> {
            log.info("线程执行任务中....");
            throw Exceptions.unknown();
        });
        Thread.sleep(2000);
    }


    @Test
    public void test01() throws InterruptedException {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
        scheduledThreadPool.scheduleAtFixedRate(() -> {
            log.info("正在执行任务");
        }, 3, 1, TimeUnit.SECONDS);
        Thread.sleep(500000);
    }


    @Test
    public void test02() throws InterruptedException {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
        ScheduledFuture<?> fixedDelay = scheduledThreadPool.scheduleWithFixedDelay(() -> {
            log.info("正在执行任务");
        }, 3, 1, TimeUnit.SECONDS);

        Thread.sleep(500000);
    }

    @Test
    public void test03() throws InterruptedException {
        ScheduledExecutorService scheduledThreadPool =
                new ScheduledThreadPoolExecutor(1, new LogExceptionThreadFactory("scheduled-thread-"),
                        new ThreadPoolExecutor.DiscardPolicy());
        ScheduledFuture<?> fixedDelay = scheduledThreadPool.scheduleWithFixedDelay(() -> {
            log.info("正在执行任务");
        }, 3, 1, TimeUnit.SECONDS);

        Thread.sleep(500000);
    }

}