package com.weweibuy.framework.rocketmq.core.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/1/8 12:19
 **/
@Slf4j
public abstract class AbstractRocketMessageListener<R> implements RocketMessageListener<R> {

    private Integer batchSize;

    private String tags;

    private RocketListenerErrorHandler errorHandler;

    private RocketHandlerMethod rocketHandlerMethod;

    public AbstractRocketMessageListener(Integer batchSize, String tags,
                                         RocketListenerErrorHandler errorHandler,
                                         RocketHandlerMethod rocketHandlerMethod) {
        this.batchSize = batchSize;
        this.errorHandler = errorHandler;
        this.rocketHandlerMethod = rocketHandlerMethod;
        this.tags = tags;
    }

    @Override
    public R onMessage(List<MessageExt> messageExtList, Object... args) {
        Object reValue = null;
        try {
            reValue = rocketHandlerMethod.invoke(messageExtList, args);
        } catch (Exception e) {
            log.error("出席异常", e);
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


    @Override
    public R onMessage(MessageExt messageExt, Object... args) {
        Object reValue = null;
        try {
            reValue = rocketHandlerMethod.invoke(messageExt, args);
        } catch (Exception e) {

        }
        return handleResult(reValue);
    }


    /**
     * 处理结果
     */
    protected R handleResult(Object reValue) {
        return null;
    }


    public Integer getBatchSize() {
        return this.batchSize;
    }

    public String getTag() {
        return this.tags;
    }


    protected abstract boolean isOrderly();

}
