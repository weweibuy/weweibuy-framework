package com.weweibuy.framework.rocketmq.core.consumer;

import java.lang.reflect.Method;

/**
 * @author durenhao
 * @date 2020/1/6 22:54
 **/
public class MethodRocketListenerEndpoint {

    private String name;

    private String topic;

    private String group;

    private String tags;

    private boolean orderly;

    private boolean timeout;

    private Integer maxRetry;

    private Integer threadMin;

    private Integer threadMax;

    private Integer consumeMessageBatchMaxSize;

    private String accessKey;

    private String secretKey;

    private String accessChannel;

    private Object bean;

    private Method method;

    private MessageHandlerMethodFactory messageHandlerMethodFactory;

}
