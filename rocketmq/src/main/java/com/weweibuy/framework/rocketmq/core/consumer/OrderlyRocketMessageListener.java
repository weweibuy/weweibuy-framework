package com.weweibuy.framework.rocketmq.core.consumer;

import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;

/**
 * @author durenhao
 * @date 2020/1/12 22:57
 **/
public class OrderlyRocketMessageListener extends AbstractRocketMessageListener<ConsumeOrderlyStatus> {

    public OrderlyRocketMessageListener(MethodRocketListenerEndpoint endpoint, RocketHandlerMethod handlerMethod) {
        super(endpoint, handlerMethod);
    }

    @Override
    protected ConsumeOrderlyStatus getSuccessReturnValue() {
        return ConsumeOrderlyStatus.SUCCESS;
    }

    @Override
    protected ConsumeOrderlyStatus getFailReturnValue() {
        return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
    }
}
