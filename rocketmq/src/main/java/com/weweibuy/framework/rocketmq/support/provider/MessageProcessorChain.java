package com.weweibuy.framework.rocketmq.support.provider;

import org.apache.rocketmq.common.message.Message;

/**
 * 处理器链
 *
 * @author durenhao
 * @date 2020/1/2 22:58
 **/
public interface MessageProcessorChain {

    /**
     * 传播message
     *
     * @param context
     * @param message
     */
    void fireMessage(ProducerContext context, Message message);


}
