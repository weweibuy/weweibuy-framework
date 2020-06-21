package com.weweibuy.framework.rocketmq.core.provider;

/**
 * 消息发送方法处理者
 *
 * @author durenhao
 * @date 2019/12/29 23:12
 **/
public interface MethodHandler {

    /**
     * 调用发送消息
     *
     * @param arg
     * @return
     * @throws Throwable
     */
    Object invoke(Object[] arg) throws Throwable;


}
