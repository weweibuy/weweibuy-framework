package com.weweibuy.framework.rocketmq.core.consumer;

/**
 * 异常处理
 *
 * @author durenhao
 * @date 2020/1/12 22:34
 **/
public interface RocketListenerErrorHandler {

    /**
     * 处理异常
     *
     * @return
     */
    Object handlerException(Exception e, Object messageObject, Boolean orderly);



}
