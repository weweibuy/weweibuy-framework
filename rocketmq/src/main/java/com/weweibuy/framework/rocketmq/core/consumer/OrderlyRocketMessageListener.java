package com.weweibuy.framework.rocketmq.core.consumer;

import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;

/**
 * @author durenhao
 * @date 2020/1/12 22:57
 **/
public class OrderlyRocketMessageListener extends AbstractRocketMessageListener<ConsumeOrderlyStatus> {

    public OrderlyRocketMessageListener(Integer batchSize, String tags, RocketListenerErrorHandler errorHandler,
                                        RocketHandlerMethod handlerMethod) {
        super(batchSize, tags, errorHandler, handlerMethod);
    }


    @Override
    protected boolean isOrderly() {
        return true;
    }
}
