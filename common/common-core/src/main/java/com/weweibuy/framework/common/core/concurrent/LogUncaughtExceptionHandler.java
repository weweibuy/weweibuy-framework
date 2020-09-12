package com.weweibuy.framework.common.core.concurrent;

import lombok.extern.slf4j.Slf4j;

/**
 * @author durenhao
 * @date 2020/9/12 21:38
 **/
@Slf4j
public class LogUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("线程: {}, 发生未捕获异常: ", t.getName(), e);
    }
}
