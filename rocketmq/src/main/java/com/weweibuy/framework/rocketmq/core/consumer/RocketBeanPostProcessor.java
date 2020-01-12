package com.weweibuy.framework.rocketmq.core.consumer;

import com.weweibuy.framework.rocketmq.annotation.RocketConsumerHandler;
import com.weweibuy.framework.rocketmq.annotation.RocketListener;
import com.weweibuy.framework.rocketmq.config.RocketMqProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
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

    private RocketMqProperties rocketMqProperties;

    public RocketBeanPostProcessor(RocketListenerContainerFactory containerFactory, RocketMqProperties rocketMqProperties) {
        this.rocketEndpointRegistrar = new RocketEndpointRegistrar(containerFactory);
        this.rocketMqProperties = rocketMqProperties;
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
        String name = rocketListener.name();
        if (StringUtils.isBlank(name)) {
            name = beanName;
        }

        listenerEndpoint.setName(name);
        listenerEndpoint.setBean(bean);
        listenerEndpoint.setNameServer(rocketMqProperties.getNameServer());
        listenerEndpoint.setMethod(method);
        listenerEndpoint.setMessageHandlerMethodFactory(messageHandlerMethodFactory);
        listenerEndpoint.setTags(consumerHandler.tags());
        listenerEndpoint.setOrderly(rocketListener.orderly());
        listenerEndpoint.setConsumeMessageBatchMaxSize(rocketListener.consumeMessageBatchMaxSize());


        if (rocketMqProperties.getConsumer() != null && rocketMqProperties.getConsumer().get(name) != null) {
            RocketMqProperties.Consumer consumer = rocketMqProperties.getConsumer().get(name);
            listenerEndpoint.setAccessChannel(consumer.getAccessChannel());
            listenerEndpoint.setAccessKey(consumer.getAccessKey());
            listenerEndpoint.setSecretKey(consumer.getSecretKey());
            listenerEndpoint.setMsgTrace(consumer.getEnableMsgTrace());
            listenerEndpoint.setTraceTopic(consumer.getCustomizedTraceTopic());

            if (StringUtils.isNotBlank(consumer.getTopic())) {
                listenerEndpoint.setTopic(consumer.getTopic());
            } else {
                listenerEndpoint.setTopic(rocketListener.topic());
            }

            if (StringUtils.isNotBlank(consumer.getGroup())) {
                listenerEndpoint.setGroup(consumer.getGroup());
            } else {
                listenerEndpoint.setGroup(rocketListener.topic());
            }

            if (consumer.getTimeout() != null) {
                listenerEndpoint.setTimeout(consumer.getTimeout());
            } else {
                listenerEndpoint.setTimeout(rocketListener.timeout());
            }

            if (consumer.getThreadMax() != null) {
                listenerEndpoint.setThreadMax(consumer.getThreadMax());
            } else {
                listenerEndpoint.setThreadMax(rocketListener.threadMax());
            }

            if (consumer.getThreadMin() != null) {
                listenerEndpoint.setThreadMin(consumer.getThreadMin());
            } else {
                listenerEndpoint.setThreadMin(rocketListener.threadMin());
            }

            if (consumer.getMaxRetry() != null) {
                listenerEndpoint.setMaxRetry(consumer.getMaxRetry());
            } else {
                listenerEndpoint.setMaxRetry(rocketListener.maxRetry());
            }

        } else {
            listenerEndpoint.setTopic(rocketListener.topic());
            listenerEndpoint.setGroup(rocketListener.topic());

            listenerEndpoint.setThreadMin(rocketListener.threadMin());
            listenerEndpoint.setThreadMax(rocketListener.threadMax());
            listenerEndpoint.setMaxRetry(rocketListener.maxRetry());
            listenerEndpoint.setTimeout(rocketListener.timeout());
        }
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
