package com.weweibuy.framework.rocketmq.core.provider;

/**
 * 发送过滤器链
 *
 * @author durenhao
 * @date 2020/2/20 20:22
 **/
public interface MessageSendFilterChain {

    /**
     * 消息发送过滤器链
     *
     * @param context
     * @param message
     * @return
     * @throws Throwable
     */
    Object doFilter(MessageSendContext context, Object message) throws Throwable;

}
