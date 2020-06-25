package com.weweibuy.framework.rocketmq.core.consumer;

import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * MQ 消费过滤器
 *
 * @author durenhao
 * @date 2020/2/22 10:46
 **/
public interface ConsumerFilter {

    /**
     * 过滤
     *
     * @param messageExtList MessageExt 或者 List<MessageExt>
     * @param originContext ConsumeConcurrentlyContext or ConsumeOrderlyContext
     * @param chain
     * @return
     */
    Object filter(List<MessageExt> messageExtList, Object originContext, MessageConsumerFilterChain chain);

    /**
     * 获取执行顺序
     *
     * @return
     */
    int getOrder();
}
