package com.weweibuy.framework.rocketmq.core.consumer;

/**
 * @author durenhao
 * @date 2020/1/17 14:42
 **/
public class DefaultMessageHandlerMethodFactory implements MessageHandlerMethodFactory {

    @Override
    public RocketHandlerMethod createHandlerMethod(MethodRocketListenerEndpoint endpoint) {
        return new RocketHandlerMethod(endpoint);
    }
}
