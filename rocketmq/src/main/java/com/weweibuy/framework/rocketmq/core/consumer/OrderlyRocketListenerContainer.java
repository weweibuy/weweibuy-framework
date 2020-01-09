/*
 * All rights Reserved, Designed By baowei
 *
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.weweibuy.framework.rocketmq.core.consumer;

import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/1/9 11:41
 **/
public class OrderlyRocketListenerContainer extends AbstractRocketListenerContainer<ConsumeOrderlyContext, ConsumeOrderlyStatus> {

    @Override
    public ConsumeOrderlyStatus consume(List<MessageExt> messageExtList, ConsumeOrderlyContext context) {
        return null;
    }
}
