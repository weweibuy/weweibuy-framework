package com.weweibuy.framework.rocketmq.core.consumer;

import lombok.Data;
import org.apache.rocketmq.client.AccessChannel;

import java.lang.reflect.Method;

/**
 * @author durenhao
 * @date 2020/1/6 22:54
 **/
@Data
public class MethodRocketListenerEndpoint {

    private String name;

    private String nameServer;

    private String topic;

    private String group;

    private String tags;

    private Boolean orderly;

    private Boolean msgTrace;

    private String traceTopic;

    private Long timeout;

    private Integer maxRetry;

    private Integer threadMin;

    private Integer threadMax;

    private Integer consumeMessageBatchMaxSize;

    private String accessKey;

    private String secretKey;

    private AccessChannel accessChannel;

    private Object bean;

    private Method method;

    private MessageHandlerMethodFactory messageHandlerMethodFactory;

    /**
     * 创建每个方法对应的监听
     *
     * @param listenerContainer
     * @return
     */
    public RocketMessageListener createRocketMessageListener(RocketListenerContainer listenerContainer) {
        RocketMessageListener listener = createListener();
        RocketHandlerMethod handlerMethod = messageHandlerMethodFactory.createHandlerMethod(bean, method);
        
        return null;
    }


    private RocketMessageListener createListener() {
        if (orderly) {
            return new OrderlyRocketMessageListener();
        }
        return new ConcurrentRocketMessageListener();
    }

}
