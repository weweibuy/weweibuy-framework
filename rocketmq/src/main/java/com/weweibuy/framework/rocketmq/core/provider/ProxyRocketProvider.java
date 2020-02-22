package com.weweibuy.framework.rocketmq.core.provider;

import com.weweibuy.framework.rocketmq.support.DefaultProxyHandlerFactory;
import com.weweibuy.framework.rocketmq.support.DefaultRocketMethodHandler;
import com.weweibuy.framework.rocketmq.support.TargetMethodMetaDataParser;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

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

    private final List<MessageSendFilter> messageSendFilterList;

    private final MQProducer mqProducer;

    public InvocationHandler newInstance(Class<?> target) {
        Map<Method, RocketMethodMetadata> parser = targetMethodMetaDataParser.parser(target);

        Map<Method, MethodHandler> methodMethodHandlerMap = new HashMap<>();

        parser.forEach((k, v) -> {
            DefaultRocketMethodHandler methodHandler = new DefaultRocketMethodHandler(v, messageQueueSelector, messageSendFilterList, mqProducer);
            methodMethodHandlerMap.put(k, methodHandler);
        });

        InvocationHandler invocationHandler = proxyHandlerFactory.create(methodMethodHandlerMap);

        return invocationHandler;
    }

    public ProxyRocketProvider(MessageQueueSelector messageQueueSelector,
                               TargetMethodMetaDataParser targetMethodMetaDataParser,
                               MQProducer mqProducer, List<MessageSendFilter> messageSendFilterList) {
        this.mqProducer = mqProducer;
        this.messageQueueSelector = messageQueueSelector;
        this.targetMethodMetaDataParser = targetMethodMetaDataParser;
        if (CollectionUtils.isEmpty(messageSendFilterList)) {
            messageSendFilterList = Collections.emptyList();
        }
        this.messageSendFilterList = messageSendFilterList.stream()
                .sorted(Comparator.comparing(MessageSendFilter::getOrder))
                .collect(Collectors.toList());
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
