package com.weweibuy.framework.rocketmq.support.provider;

import org.apache.rocketmq.common.message.Message;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/1/2 23:00
 **/
public class DefaultMessageProcessorChain implements MessageProcessorChain {

    private List<MessagePostProcessor> postProcessorList;

    @Override
    public void fireMessage(ProducerContext context, Message message) {
        Integer processorTotal = context.getProcessorTotal();
        Integer pos = context.getPos();
        if (pos < processorTotal) {
            MessagePostProcessor messagePostProcessor = postProcessorList.get(pos++);
            messagePostProcessor.processor(context, message, this);
            return;
        }



    }

}
