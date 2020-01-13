package com.weweibuy.framework.rocketmq.core.consumer;

import com.weweibuy.framework.rocketmq.core.MessageConverter;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;

/**
 * @author durenhao
 * @date 2020/1/12 22:56
 **/
public class ConcurrentRocketMessageListener extends AbstractRocketMessageListener<ConsumeConcurrentlyStatus> {

    public ConcurrentRocketMessageListener(Integer batchSize, MessageConverter messageConverter,
                                           RocketListenerErrorHandler errorHandler, RocketHandlerMethod handlerMethod) {
        super(batchSize, messageConverter, errorHandler, handlerMethod);
    }


    @Override
    protected boolean isOrderly() {
        return false;
    }


}
