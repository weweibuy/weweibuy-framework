package com.weweibuy.framework.rocketmq.core.consumer;

/**
 * Mq消息监听
 *
 * @author durenhao
 * @date 2020/1/8 11:14
 **/
public interface  RocketMessageListener<R> {

    /**
     * 处理消息
     *
     * @param messageObject 消息体   MessageExt 或者 List<MessageExt>
     * @param originContext 原始消费上下文  ConsumeConcurrentlyContext or ConsumeOrderlyContext
     * @return
     */
    R onMessage(Object messageObject, Object originContext);

}
