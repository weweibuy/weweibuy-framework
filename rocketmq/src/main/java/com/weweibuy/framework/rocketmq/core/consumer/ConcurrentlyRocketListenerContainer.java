package com.weweibuy.framework.rocketmq.core.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.Map;

/**
 * 监听器容器
 *
 * @author durenhao
 * @date 2020/1/8 10:54
 **/
public class ConcurrentlyRocketListenerContainer extends AbstractRocketListenerContainer<ConsumeConcurrentlyContext, ConsumeConcurrentlyStatus> {

    private List<RocketMessageListener> rocketMessageListenerList;

    private Map<String, RocketMessageListener> messageListenerMap;

    private MessageListener messageListener = new MessageListenerConcurrently() {
        @Override
        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageExtList, ConsumeConcurrentlyContext context) {
            return consume(messageExtList, context);
        }
    };

    public ConcurrentlyRocketListenerContainer(DefaultMQPushConsumer mqPushConsumer) {
        super(mqPushConsumer);
    }

    @Override
    protected MessageListener getMessageListener() {
        return this.messageListener;
    }


    @Override
    public ConsumeConcurrentlyStatus consume(List<MessageExt> list, ConsumeConcurrentlyContext context) {
        // 消费消息
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
