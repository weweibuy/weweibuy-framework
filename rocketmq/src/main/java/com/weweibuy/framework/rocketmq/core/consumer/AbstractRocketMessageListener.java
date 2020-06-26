package com.weweibuy.framework.rocketmq.core.consumer;

import com.weweibuy.framework.rocketmq.support.RocketMqLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
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

    public AbstractRocketMessageListener(MethodRocketListenerEndpoint endpoint, RocketHandlerMethod handlerMethod) {
        this.batchSize = endpoint.getConsumeMessageBatchMaxSize();
        this.errorHandler = endpoint.getErrorHandler();
        this.rocketHandlerMethod = handlerMethod;
        this.tags = endpoint.getTags();
    }

    @Override
    public R onMessage(Object messageObject, Object originContext) {
        Object reValue = null;
        Exception exception = null;
        try {
            reValue = rocketHandlerMethod.invoke(messageObject, originContext);
            return handleResult(reValue);
        } catch (InvocationTargetException ex) {
            exception = (Exception) ex.getTargetException();
        } catch (Exception e) {
            exception = e;
        }
        return handlerException(exception, messageObject, originContext);
    }

    private R handlerException(Exception throwable, Object messageObject, Object originContext) {
        if (errorHandler != null) {
            if (errorHandler.handlerException(throwable, messageObject, originContext)) {
                return getSuccessReturnValue();
            }
        } else {
            if (messageObject instanceof Collection) {
                logException((List) messageObject, originContext, throwable);
            } else if (messageObject instanceof MessageExt) {
                logException(Collections.singletonList((MessageExt) messageObject), originContext, throwable);
            } else {
                log.warn("消费MQ消息: {}, 时异常: {}", messageObject, throwable);
            }
        }
        return getFailReturnValue();
    }

    private void logException(List<MessageExt> messageExtList, Object originContext, Exception e) {
        RocketMqLogger.logConsumerException(messageExtList, originContext, e);
    }

    /**
     * 处理结果
     */
    protected R handleResult(Object reValue) {
        if (reValue == null) {
            return getSuccessReturnValue();
        }
        if (getSuccessReturnValue().equals(reValue)) {
            return getSuccessReturnValue();
        }
        if (getFailReturnValue().equals(reValue)) {
            return getFailReturnValue();
        }
        return getSuccessReturnValue();
    }


    public Integer getBatchSize() {
        return this.batchSize;
    }

    public String getTag() {
        return this.tags;
    }


    /**
     * 消费成功返回值 CONSUME_SUCCESS or   ConsumeOrderlyStatus.SUCCESS
     *
     * @return
     */
    protected abstract R getSuccessReturnValue();

    /**
     * 消费失败返回值   ConsumeConcurrentlyStatus.RECONSUME_LATER or ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT
     *
     * @return
     */
    protected abstract R getFailReturnValue();


}
