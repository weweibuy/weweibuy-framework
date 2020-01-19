package com.weweibuy.framework.rocketmq.core.consumer;

import com.weweibuy.framework.rocketmq.annotation.BatchHandlerModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 监听器容器
 *
 * @author durenhao
 * @date 2020/1/8 10:54
 **/
@Slf4j
public class ConcurrentlyRocketListenerContainer extends AbstractRocketListenerContainer<ConsumeConcurrentlyContext, ConsumeConcurrentlyStatus> {

    private MessageListener messageListener;

    public ConcurrentlyRocketListenerContainer(DefaultMQPushConsumer mqPushConsumer, Integer batchSize, BatchHandlerModel batchHandlerModel) {
        super(mqPushConsumer, batchSize, batchHandlerModel, ConsumeConcurrentlyStatus.CONSUME_SUCCESS, ConsumeConcurrentlyStatus.RECONSUME_LATER);
    }


    @Override
    protected MessageListener getMessageListener() {
        if (this.messageListener == null) {
            messageListener = new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageExtList, ConsumeConcurrentlyContext context) {
                    return consume(messageExtList, context);
                }
            };
        }
        return messageListener;
    }

    @Override
    protected boolean isSuccess(ConsumeConcurrentlyStatus consumeConcurrentlyStatus) {
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS.equals(consumeConcurrentlyStatus);
    }


}
