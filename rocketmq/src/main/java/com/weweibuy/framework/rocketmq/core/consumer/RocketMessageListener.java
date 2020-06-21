package com.weweibuy.framework.rocketmq.core.consumer;

/**
 * Mq消息监听
 *
 * @author durenhao
 * @date 2020/1/8 11:14
 **/
public interface RocketMessageListener<R> {

    /**
     * @param messageObject 消息体
     * @param originContext 原始消费上下文
     * @return
     */
    R onMessage(Object messageObject, Object originContext);


}
