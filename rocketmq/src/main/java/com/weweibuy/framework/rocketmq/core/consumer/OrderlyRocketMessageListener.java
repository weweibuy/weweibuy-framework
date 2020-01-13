package com.weweibuy.framework.rocketmq.core.consumer;

import com.weweibuy.framework.rocketmq.core.MessageConverter;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;

/**
 * @author durenhao
 * @date 2020/1/12 22:57
 **/
public class OrderlyRocketMessageListener extends AbstractRocketMessageListener<ConsumeOrderlyStatus> {

    public OrderlyRocketMessageListener(Integer batchSize, MessageConverter messageConverter,
                                        RocketListenerErrorHandler errorHandler, RocketHandlerMethod handlerMethod) {
        super(batchSize, messageConverter, errorHandler, handlerMethod);
    }


    @Override
    protected boolean isOrderly() {
        return true;
    }
}
