package com.weweibuy.framework.rocketmq.core.consumer;

import com.weweibuy.framework.rocketmq.annotation.RocketConsumerHandler;
import com.weweibuy.framework.rocketmq.annotation.RocketListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author durenhao
 * @date 2020/1/4 20:31
 **/
public class RocketBeanPostProcessor implements BeanPostProcessor, SmartInitializingSingleton {

    private RocketEndpointRegistrar rocketEndpointRegistrar;

    private List<MethodRocketListenerEndpoint> endpointList;

    private MessageHandlerMethodFactory messageHandlerMethodFactory;


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        RocketListener listenerAnnotation = findListenerAnnotation(bean.getClass());
        if (listenerAnnotation != null) {
            processRocketHandler(bean, beanName, listenerAnnotation);
        }
        return bean;
    }


    private void processRocketHandler(Object bean, String beanName, RocketListener rocketListener) {
        ReflectionUtils.doWithLocalMethods(bean.getClass(), method -> {
            RocketConsumerHandler annotation = findListenerAnnotation(method);
            if (annotation != null) {
                MethodRocketListenerEndpoint endpoint = buildEndpoint(rocketListener, annotation, bean, beanName, method);
                if (endpointList == null) {
                    endpointList = new ArrayList<>();
                }
                endpointList.add(endpoint);
            }
        });
    }

    private MethodRocketListenerEndpoint buildEndpoint(RocketListener rocketListener, RocketConsumerHandler consumerHandler,
                                                       Object bean, String beanName, Method method) {
        MethodRocketListenerEndpoint listenerEndpoint = new MethodRocketListenerEndpoint();
        listenerEndpoint.setName(beanName);
        listenerEndpoint.setTopic(rocketListener.topic());
        listenerEndpoint.setGroup(rocketListener.group());
        listenerEndpoint.setThreadMin(rocketListener.threadMin());
        listenerEndpoint.setThreadMax(rocketListener.threadMax());
        listenerEndpoint.setBean(bean);
        listenerEndpoint.setAccessChannel(rocketListener.accessChannel());
        listenerEndpoint.setAccessKey(rocketListener.accessKey());
        listenerEndpoint.setMethod(method);
        listenerEndpoint.setMaxRetry(rocketListener.maxRetry());
        listenerEndpoint.setConsumeMessageBatchMaxSize(rocketListener.consumeMessageBatchMaxSize());
        listenerEndpoint.setOrderly(rocketListener.orderly());
        listenerEndpoint.setTimeout(rocketListener.timeout());
        listenerEndpoint.setTags(consumerHandler.tags());
        listenerEndpoint.setMessageHandlerMethodFactory(messageHandlerMethodFactory);
        return listenerEndpoint;
    }

    private RocketConsumerHandler findListenerAnnotation(Method method) {
        return AnnotationUtils.findAnnotation(method, RocketConsumerHandler.class);
    }


    private RocketListener findListenerAnnotation(Class<?> clazz) {
        return AnnotationUtils.findAnnotation(clazz, RocketListener.class);
    }


    @Override
    public void afterSingletonsInstantiated() {
        if (CollectionUtils.isEmpty(endpointList)) {
            return;
        }

        // 注册容器
    }


    private void validateEndpoint(List<MethodRocketListenerEndpoint> endpointList) {
    }


}
