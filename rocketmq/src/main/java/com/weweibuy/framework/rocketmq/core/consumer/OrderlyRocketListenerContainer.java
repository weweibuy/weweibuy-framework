package com.weweibuy.framework.rocketmq.core.consumer;

import com.weweibuy.framework.rocketmq.annotation.BatchHandlerModel;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/1/9 11:41
 **/
public class OrderlyRocketListenerContainer extends AbstractRocketListenerContainer<ConsumeOrderlyContext, ConsumeOrderlyStatus> {


    public OrderlyRocketListenerContainer(DefaultMQPushConsumer mqPushConsumer, Integer batchSize, BatchHandlerModel batchHandlerModel) {
        super(mqPushConsumer, batchSize, batchHandlerModel);
    }

    @Override
    protected MessageListener getMessageListener() {
        return null;
    }

    @Override
    public ConsumeOrderlyStatus consume(List<MessageExt> messageExtList, ConsumeOrderlyContext context) {
        return null;
    }
}
