package com.weweibuy.framework.rocketmq.core.consumer;

import com.weweibuy.framework.rocketmq.annotation.RocketConsumerHandler;
import com.weweibuy.framework.rocketmq.annotation.RocketListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author durenhao
 * @date 2020/1/4 20:31
 **/
public class RocketBeanPostProcessor implements BeanPostProcessor, SmartInitializingSingleton, DisposableBean {


    private List<MethodRocketListenerEndpoint> endpointList;

    private MessageHandlerMethodFactory messageHandlerMethodFactory;

    private RocketEndpointRegistrar rocketEndpointRegistrar;

    public RocketBeanPostProcessor(RocketListenerContainerFactory containerFactory) {
        this.rocketEndpointRegistrar = new RocketEndpointRegistrar(containerFactory);
    }


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
        validateEndpoint();
        // 注册容器
        rocketEndpointRegistrar.setEndpointList(endpointList);
        rocketEndpointRegistrar.registerListenerContainer();
        rocketEndpointRegistrar.startUpAllContainer();
    }


    private void validateEndpoint() {
        Map<String, List<MethodRocketListenerEndpoint>> endpointMap = endpointList.stream()
                .collect(Collectors.groupingBy(e -> e.getGroup() + "_" + e.getTopic()));

        endpointMap.forEach((k, v) -> {
            if (v.size() > 1) {
                List<String> tagList = new ArrayList<>();
                Map<SameRocketEndpointKey, List<MethodRocketListenerEndpoint>> collect = v.stream()
                        .peek(e -> Assert.isTrue(!"*".equals(e.getTags()), "同一Topic与Group的RocketListener, 其中RocketConsumerHandler的Tag不能为 *"))
                        .peek(e -> tagList.add(e.getTags()))
                        .collect(Collectors.groupingBy(this::toRocketEndpointKey));
                Assert.isTrue(collect.size() == 1, "同一Topic与Group的RocketListener, orderly,timeout,maxRetry,threadMin" +
                        "threadMax,consumeMessageBatchMaxSize,accessKey,secretKey,accessChannel 必须相同");

                for (int i = 0; i < tagList.size(); i++) {
                    for (int j = 0; j < tagList.size(); j++) {
                        if (i == j) {
                            continue;
                        }
                        String tagI = tagList.get(i);
                        String tagJ = tagList.get(j);
                        checkTag(tagI, tagJ);
                    }

                }
            }
        });
    }

    private void checkTag(String tag1, String tag2) {
        String[] split1 = tag1.split("\\|\\|");
        String[] split2 = tag2.split("\\|\\|");
        Set<String> hashSet = new HashSet<>();
        Arrays.stream(split1).forEach(hashSet::add);
        Arrays.stream(split2).forEach(hashSet::add);
        Assert.isTrue(hashSet.size() == split1.length + split2.length, "同一Topic与Group的RocketListener, 其中RocketConsumerHandler的Tag必须不同");
    }

    private SameRocketEndpointKey toRocketEndpointKey(MethodRocketListenerEndpoint endpoint) {
        SameRocketEndpointKey key = new SameRocketEndpointKey();
        BeanUtils.copyProperties(endpoint, key);
        return key;
    }

    @Override
    public void destroy() throws Exception {
        rocketEndpointRegistrar.shutdownAllContainer();
    }

    @Data
    @EqualsAndHashCode
    private static class SameRocketEndpointKey {

        private boolean orderly;

        private Long timeout;

        private Integer maxRetry;

        private Integer threadMin;

        private Integer threadMax;

        private Integer consumeMessageBatchMaxSize;

        private String accessKey;

        private String secretKey;

        private String accessChannel;

    }

}
