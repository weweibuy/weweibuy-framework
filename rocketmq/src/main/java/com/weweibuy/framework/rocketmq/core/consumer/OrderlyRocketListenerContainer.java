package com.weweibuy.framework.rocketmq.core.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/1/9 11:41
 **/
public class OrderlyRocketListenerContainer extends AbstractRocketListenerContainer<ConsumeOrderlyContext, ConsumeOrderlyStatus> {

    private MessageListener messageListener;

    public OrderlyRocketListenerContainer(DefaultMQPushConsumer mqPushConsumer, MethodRocketListenerEndpoint endpoint) {
        super(mqPushConsumer, endpoint, ConsumeOrderlyStatus.SUCCESS, ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT);
    }

    @Override
    protected MessageListener getMessageListener() {
        if (this.messageListener == null) {
            messageListener = new MessageListenerOrderly() {
                @Override
                public ConsumeOrderlyStatus consumeMessage(List<MessageExt> messageExtList, ConsumeOrderlyContext context) {
                    return consume(messageExtList, context);
                }
            };
        }
        return messageListener;
    }


}
