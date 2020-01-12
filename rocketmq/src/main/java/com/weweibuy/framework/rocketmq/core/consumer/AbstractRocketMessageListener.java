package com.weweibuy.framework.rocketmq.core.consumer;

import com.weweibuy.framework.rocketmq.core.MessageConverter;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/1/8 12:19
 **/
public abstract class AbstractRocketMessageListener<R> implements RocketMessageListener<R> {

    private Boolean orderly;

    private Integer batchSize;

    private MessageConverter messageConverter;

    private RocketListenerErrorHandler errorHandler;


    @Override
    public R onMessage(List<MessageExt> messageExtList) {
        Object reValue = null;
        try {
            reValue = doOnMessage(messageExtList);
        } catch (Exception e) {
            if (errorHandler != null) {
                return (R) errorHandler.handlerException(e, orderly);
            } else if (orderly) {
                return (R) ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            } else {
                return (R) ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }

        }
        return handleResult();
    }


    protected abstract R doOnMessage(List<MessageExt> messageExtList);


    /**
     * 处理结果
     */
    protected R handleResult() {
        return null;
    }


    /**
     * 解析消息
     *
     * @param message
     * @return
     */
    protected Object extractMessage(Message message) {
        if (messageConverter != null) {
            return messageConverter.fromMessageBody(null, null);
        }
        return message;
    }

}
