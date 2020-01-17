package com.weweibuy.framework.rocketmq.core.consumer;

/**
 * @author durenhao
 * @date 2020/1/6 23:14
 **/
public interface MessageHandlerMethodFactory {

    /**
     * 创建,处理方法
     *
     * @param endpoint
     * @return
     */
    RocketHandlerMethod createHandlerMethod(MethodRocketListenerEndpoint endpoint);

}
