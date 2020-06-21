package com.weweibuy.framework.rocketmq.core.consumer;

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
     * @param messageObject MessageExt 或者 List<MessageExt>
     * @param originContext ConsumeConcurrentlyContext or ConsumeOrderlyContext
     * @param chain
     * @return
     */
    Object filter(Object messageObject, Object originContext, MessageConsumerFilterChain chain);

    /**
     * 获取执行顺序
     *
     * @return
     */
    int getOrder();
}
