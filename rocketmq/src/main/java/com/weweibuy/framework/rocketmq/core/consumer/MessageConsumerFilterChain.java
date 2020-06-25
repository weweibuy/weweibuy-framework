package com.weweibuy.framework.rocketmq.core.consumer;

import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/2/22 10:48
 **/
public interface MessageConsumerFilterChain {

    /**
     * 消息过滤器链
     *
     * @param messageExtList
     * @param originContext  ConsumeConcurrentlyContext or ConsumeOrderlyContext
     * @return
     */
    Object doFilter(List<MessageExt> messageExtList, Object originContext);


}
