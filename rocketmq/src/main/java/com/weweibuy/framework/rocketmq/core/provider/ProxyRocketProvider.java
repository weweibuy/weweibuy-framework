package com.weweibuy.framework.rocketmq.core.provider;

import com.weweibuy.framework.rocketmq.support.DefaultProxyHandlerFactory;
import com.weweibuy.framework.rocketmq.support.DefaultRocketMethodHandler;
import com.weweibuy.framework.rocketmq.support.TargetMethodMetaDataParser;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
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

    private final MessageQueueSelector messageQueueSelector;

    private final TargetMethodMetaDataParser targetMethodMetaDataParser;

    private final MQProducer mqProducer;

    public ProxyRocketProvider(MessageQueueSelector messageQueueSelector,
                               TargetMethodMetaDataParser targetMethodMetaDataParser,
                               MQProducer mqProducer) {
        this.messageQueueSelector = messageQueueSelector;
        this.targetMethodMetaDataParser = targetMethodMetaDataParser;
        this.mqProducer = mqProducer;
    }

    public InvocationHandler newInstance(Class<?> target) {
        Map<Method, RocketMethodMetadata> parser = targetMethodMetaDataParser.parser(target);

        Map<Method, MethodHandler> methodMethodHandlerMap = new HashMap<>();

        parser.forEach((k, v) -> {
            DefaultRocketMethodHandler methodHandler = new DefaultRocketMethodHandler(mqProducer, v, messageQueueSelector);
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
