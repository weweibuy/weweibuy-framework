package com.weweibuy.framework.rocketmq.core.consumer;

import java.lang.reflect.Method;

/**
 * @author durenhao
 * @date 2020/1/6 23:14
 **/
public interface MessageHandlerMethodFactory {

    /**
     * 创建,处理方法
     *
     * @param bean
     * @param method
     * @return
     */
    RocketHandlerMethod createHandlerMethod(Object bean, Method method,  HandlerMethodArgumentResolverComposite argumentResolverComposite);

}
