package com.weweibuy.framework.rocketmq.core.consumer;

import com.weweibuy.framework.rocketmq.annotation.BatchHandlerModel;
import lombok.Data;
import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

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

    private BatchHandlerModel batchHandlerModel;

    private String accessKey;

    private String secretKey;

    private AccessChannel accessChannel;

    private MessageModel messageModel;

    private Object bean;

    private Method method;

    private RocketListenerErrorHandler errorHandler;

    private MessageHandlerMethodFactory messageHandlerMethodFactory;

    private HandlerMethodArgumentResolverComposite argumentResolverComposite;

    /**
     * 创建每个方法对应的监听
     *
     * @param listenerContainer
     * @return
     */
    public RocketMessageListener createRocketMessageListener(RocketListenerContainer listenerContainer) {
        RocketHandlerMethod handlerMethod = messageHandlerMethodFactory.createHandlerMethod(this);

        RocketMessageListener listener = createListener(handlerMethod);

        return listener;
    }


    private RocketMessageListener createListener(RocketHandlerMethod handlerMethod) {
        if (orderly) {
            return new OrderlyRocketMessageListener(consumeMessageBatchMaxSize, tags, errorHandler, handlerMethod);
        }
        return new ConcurrentRocketMessageListener(consumeMessageBatchMaxSize, tags, errorHandler, handlerMethod);
    }

}
