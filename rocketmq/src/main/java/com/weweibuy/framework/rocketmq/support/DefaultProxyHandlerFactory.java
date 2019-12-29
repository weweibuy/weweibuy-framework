package com.weweibuy.framework.rocketmq.support;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 代理创建工厂
 *
 * @author durenhao
 * @date 2019/12/29 23:15
 **/
public class DefaultProxyHandlerFactory implements ProxyHandlerFactory {

    @Override
    public InvocationHandler create(Map<Method, MethodHandler> methodMethodHandlerMap) {
        return new RocketProviderProxy(methodMethodHandlerMap);
    }
}
