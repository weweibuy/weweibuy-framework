package com.weweibuy.framework.rocketmq.core.consumer;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;

/**
 * @author durenhao
 * @date 2020/1/12 22:56
 **/
public class ConcurrentRocketMessageListener extends AbstractRocketMessageListener<ConsumeConcurrentlyStatus> {

    public ConcurrentRocketMessageListener(Integer batchSize, String tags, RocketListenerErrorHandler errorHandler,
                                           RocketHandlerMethod handlerMethod) {
        super(batchSize, tags, errorHandler, handlerMethod);
    }


    @Override
    protected boolean isOrderly() {
        return false;
    }

    @Override
    protected ConsumeConcurrentlyStatus getSuccessReturnValue() {
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    @Override
    protected ConsumeConcurrentlyStatus getFailReturnValue() {
        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
    }


}
