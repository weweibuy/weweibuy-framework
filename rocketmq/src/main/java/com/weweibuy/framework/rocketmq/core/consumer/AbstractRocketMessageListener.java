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

    private Integer batchSize;

    private MessageConverter messageConverter;

    private RocketListenerErrorHandler errorHandler;

    private RocketHandlerMethod rocketHandlerMethod;

    public AbstractRocketMessageListener(Integer batchSize, MessageConverter messageConverter,
                                         RocketListenerErrorHandler errorHandler,
                                         RocketHandlerMethod rocketHandlerMethod) {
        this.batchSize = batchSize;
        this.messageConverter = messageConverter;
        this.errorHandler = errorHandler;
        this.rocketHandlerMethod = rocketHandlerMethod;
    }

    @Override
    public R onMessage(List<MessageExt> messageExtList) {
        Object reValue = null;
        try {
            reValue = rocketHandlerMethod.invoke(messageExtList);
        } catch (Exception e) {
            if (errorHandler != null) {
                return (R) errorHandler.handlerException(e, isOrderly());
            } else if (isOrderly()) {
                return (R) ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            } else {
                return (R) ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }

        }
        return handleResult(reValue);
    }


    /**
     * 处理结果
     */
    protected R handleResult(Object reValue) {
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

    public Integer getBatchSize() {
        return this.batchSize;
    }

    protected abstract boolean isOrderly();

}
