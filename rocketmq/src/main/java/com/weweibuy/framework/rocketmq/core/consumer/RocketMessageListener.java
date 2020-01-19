package com.weweibuy.framework.rocketmq.core.consumer;

/**
 * @author durenhao
 * @date 2020/1/8 11:14
 **/
public interface RocketMessageListener<R> {

    /**
     * 处理消息
     *
     * @param messageObject
     * @return
     */
    R onMessage(Object messageObject, Object... args);


}
