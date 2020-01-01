package com.weweibuy.framework.rocketmq.support;

import com.weweibuy.framework.rocketmq.core.RocketMethodMetadata;
import org.apache.rocketmq.client.producer.MQProducer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 代理的rocketMQ 生产者
 *
 * @author durenhao
 * @date 2019/12/29 23:17
 **/
public class ProxyRocketProvider {

    private final ProxyHandlerFactory proxyHandlerFactory;

    private final TargetMethodMetaDataParser targetMethodMetaDataParser;

    private final MQProducer mqProducer;

    public ProxyRocketProvider(ProxyHandlerFactory proxyHandlerFactory, TargetMethodMetaDataParser targetMethodMetaDataParser, MQProducer mqProducer) {
        this.proxyHandlerFactory = proxyHandlerFactory;
        this.targetMethodMetaDataParser = targetMethodMetaDataParser;
        this.mqProducer = mqProducer;
    }

    public InvocationHandler newInstance(Class<?> target) {
        Map<Method, RocketMethodMetadata> parser = targetMethodMetaDataParser.parser(target);

        Map<Method, MethodHandler> methodMethodHandlerMap = new HashMap<>();

        parser.forEach((k, v) -> {
            DefaultRocketMethodHandler methodHandler = new DefaultRocketMethodHandler(mqProducer, v);
            methodMethodHandlerMap.put(k, methodHandler);
        });

        InvocationHandler invocationHandler = proxyHandlerFactory.create(methodMethodHandlerMap);

        return invocationHandler;
    }


}
