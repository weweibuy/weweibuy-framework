package com.weweibuy.framework.common.core.concurrent;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.concurrent.Callable;

/**
 * 使用该类包装任务,输出异常日志
 * eg: ScheduledExecutorService 任务出现异常不会输出线程未捕获异常日志
 *
 * @author durenhao
 * @date 2023/8/3 15:40
 **/
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskExceptionWarp {

    public static Runnable warpRunnable(Runnable runnable) {
        return () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                logException(e);
                throw e;
            }
        };
    }


    public static <T> Callable<T> warpCallable(Callable<T> callable) {
        return () -> {
            try {
                return callable.call();
            } catch (Exception e) {
                logException(e);
                throw e;
            }
        };
    }


    private static void logException(Exception e) {
        Thread thread = Thread.currentThread();
        log.error("调度线程: {}, 发生异常: ", thread.getName(), e);
    }

}
