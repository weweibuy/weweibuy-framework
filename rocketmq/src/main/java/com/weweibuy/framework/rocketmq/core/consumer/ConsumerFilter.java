package com.weweibuy.framework.rocketmq.core.consumer;

/**
 * @author durenhao
 * @date 2020/2/22 10:46
 **/
public interface ConsumerFilter {

    Object filter(Object messageObject, Object originContext, MessageConsumerFilterChain chain);

    int getOrder();
}
