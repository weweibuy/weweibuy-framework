package com.weweibuy.framework.rocketmq.support.provider;

import org.apache.rocketmq.common.message.Message;

/**
 * 消息发送前处理器
 *
 * @author durenhao
 * @date 2020/1/2 17:35
 **/
public interface MessagePostProcessor {

    /**
     * 处理消息
     *
     * @param producerContext
     * @param message
     */
    void processor(ProducerContext producerContext, Message message, MessageProcessorChain chain);

    /**
     * 获取顺序
     *
     * @return
     */
    Integer getOrder();

}
