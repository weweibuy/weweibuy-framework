package com.weweibuy.framework.rocketmq.core.consumer;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/1/12 22:56
 **/
public class ConcurrentRocketMessageListener extends AbstractRocketMessageListener<ConsumeConcurrentlyStatus> {

    @Override
    protected ConsumeConcurrentlyStatus doOnMessage(List<MessageExt> messageExtList) {
        return null;
    }
}
