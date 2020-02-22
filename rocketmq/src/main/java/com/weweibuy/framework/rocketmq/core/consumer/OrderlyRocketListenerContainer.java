package com.weweibuy.framework.rocketmq.core.consumer;

import com.weweibuy.framework.rocketmq.annotation.BatchHandlerModel;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/1/9 11:41
 **/
public class OrderlyRocketListenerContainer extends AbstractRocketListenerContainer<ConsumeOrderlyContext, ConsumeOrderlyStatus> {

    private MessageListener messageListener;

    public OrderlyRocketListenerContainer(DefaultMQPushConsumer mqPushConsumer, Integer batchSize, BatchHandlerModel batchHandlerModel, List<ConsumerFilter> filterList) {
        super(mqPushConsumer, batchSize, batchHandlerModel, filterList, ConsumeOrderlyStatus.SUCCESS, ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT);
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

    @Override
    protected boolean isSuccess(ConsumeOrderlyStatus consumeOrderlyStatus) {
        return ConsumeOrderlyStatus.SUCCESS.equals(consumeOrderlyStatus);
    }

}
