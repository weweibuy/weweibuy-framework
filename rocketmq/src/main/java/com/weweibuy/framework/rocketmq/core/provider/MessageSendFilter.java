package com.weweibuy.framework.rocketmq.core.provider;

import org.apache.rocketmq.client.producer.MQProducer;

/**
 * @author durenhao
 * @date 2020/2/20 20:24
 **/
public interface MessageSendFilter {

    /**
     * 过滤
     *
     * @param context
     * @param message
     * @param chain
     * @return
     * @throws Throwable
     */
    Object filter(MessageSendContext context, Object message, MQProducer mqProducer,  MessageSendFilterChain chain) throws Throwable;

    /**
     * 获取 顺序
     *
     * @return
     */
    Integer getOrder();


}
