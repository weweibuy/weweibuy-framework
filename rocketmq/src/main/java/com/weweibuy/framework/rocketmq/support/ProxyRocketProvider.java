package com.weweibuy.framework.rocketmq.support;

import com.weweibuy.framework.rocketmq.core.RocketMethodMetadata;
import org.apache.rocketmq.client.producer.MQProducer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

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
public class ProxyRocketProvider implements InitializingBean, DisposableBean {

    private final ProxyHandlerFactory proxyHandlerFactory = new DefaultProxyHandlerFactory();

    private final TargetMethodMetaDataParser targetMethodMetaDataParser;

    private final MQProducer mqProducer;

    public ProxyRocketProvider(TargetMethodMetaDataParser targetMethodMetaDataParser, MQProducer mqProducer) {
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


    @Override
    public void afterPropertiesSet() throws Exception {
        mqProducer.start();
    }

    @Override
    public void destroy() throws Exception {
        mqProducer.shutdown();
    }
}
