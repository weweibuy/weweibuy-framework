package com.weweibuy.framework.rocketmq.core.consumer;

import lombok.extern.slf4j.Slf4j;

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
                return getFailReturnValue();
            } else {
                return getFailReturnValue();
            }
        }
        return handleResult(reValue);
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
