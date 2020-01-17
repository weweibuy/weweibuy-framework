/*
 * All rights Reserved, Designed By baowei
 *
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.weweibuy.framework.rocketmq.core.consumer;

import java.lang.reflect.Method;

/**
 * @author durenhao
 * @date 2020/1/17 14:42
 **/
public class DefaultMessageHandlerMethodFactory implements MessageHandlerMethodFactory {

    @Override
    public RocketHandlerMethod createHandlerMethod(Object bean, Method method, HandlerMethodArgumentResolverComposite argumentResolverComposite) {
        return new RocketHandlerMethod(bean, method, argumentResolverComposite);
    }
}
