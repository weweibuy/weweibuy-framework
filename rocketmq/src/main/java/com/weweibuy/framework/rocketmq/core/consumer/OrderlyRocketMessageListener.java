package com.weweibuy.framework.rocketmq.core.consumer;

import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/1/12 22:57
 **/
public class OrderlyRocketMessageListener extends AbstractRocketMessageListener<ConsumeOrderlyStatus> {


    @Override
    protected ConsumeOrderlyStatus doOnMessage(List<MessageExt> messageExtList) {
        return null;
    }
}
