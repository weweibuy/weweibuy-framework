package com.weweibuy.framework.rocketmq.core.provider;

/**
 * MQ消息发送过滤器
 *
 * @author durenhao
 * @date 2020/2/20 20:24
 **/
public interface MessageSendFilter {

    /**
     * 过滤
     *
     * @param context message上下文
     * @param message  Message or Collection<Message>
     * @param chain
     * @return
     * @throws Throwable
     */
    Object filter(MessageSendContext context, Object message, MessageSendFilterChain chain) throws Throwable;

    /**
     * 获取 顺序
     *
     * @return
     */
    Integer getOrder();


}
