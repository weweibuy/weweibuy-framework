package com.weweibuy.framework.rocketmq.support;

import org.apache.rocketmq.client.producer.MQProducer;

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

    InvocationHandler create(MQProducer producer, Map<Method, MethodHandler> methodMethodHandlerMap);


    interface MethodHandler {

        Object invoke(Object[] arg) throws Throwable;
    }

}
