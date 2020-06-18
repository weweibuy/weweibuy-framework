package com.weweibuy.framework.rocketmq.core.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;

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

    public AbstractRocketMessageListener(Integer batchSize, String tags,
                                         RocketListenerErrorHandler errorHandler,
                                         RocketHandlerMethod rocketHandlerMethod) {
        this.batchSize = batchSize;
        this.errorHandler = errorHandler;
        this.rocketHandlerMethod = rocketHandlerMethod;
        this.tags = tags;
    }

    @Override
    public R onMessage(Object messageObject, Object originContext) {
        Object reValue = null;
        try {
            reValue = rocketHandlerMethod.invoke(messageObject, originContext);
        } catch (Exception e) {
            if (errorHandler != null) {
                if (errorHandler.handlerException(e, messageObject, isOrderly())) {
                    return getSuccessReturnValue();
                }
            } else {
                if (messageObject instanceof Collection) {
                    logException((List) messageObject, e);
                } else if (messageObject instanceof MessageExt) {
                    logException(Collections.singletonList((MessageExt) messageObject), e);
                } else {
                    log.warn("消费MQ消息: {}, 时异常: {}", messageObject, e);
                }
            }
            return getFailReturnValue();
        }
        return handleResult(reValue);
    }


    private void logException(List<MessageExt> messageExtList, Exception e) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("消费MQ消息: ");
        for (int i = 0; i < messageExtList.size(); i++) {
            stringBuilder.append(" Topic:【").append(messageExtList.get(i).getTags()).append("】")
                    .append(" Tag:【").append(messageExtList.get(i).getTags()).append("】")
                    .append(" Key:【").append(messageExtList.get(i).getKeys()).append("】")
                    .append(" Body:【").append(new String(messageExtList.get(i).getBody())).append("】");
            if (i != messageExtList.size()) {
                stringBuilder.append("\r\n");
            }
        }
        stringBuilder.append("出现异常: ");
        log.warn(stringBuilder.toString(), e);
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


    protected abstract boolean isOrderly();

    protected abstract R getSuccessReturnValue();

    protected abstract R getFailReturnValue();


}
