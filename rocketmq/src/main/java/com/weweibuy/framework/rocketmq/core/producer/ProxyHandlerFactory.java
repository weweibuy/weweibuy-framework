package com.weweibuy.framework.rocketmq.core.producer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 代理创建工厂
 *
 * @author durenhao
 * @date 2019/12/29 22:48
 **/
public interface ProxyHandlerFactory {

    InvocationHandler create(Map<Method, MethodHandler> methodMethodHandlerMap);


}
