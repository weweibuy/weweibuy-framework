package com.weweibuy.framework.rocketmq.core.consumer;

import com.weweibuy.framework.rocketmq.annotation.BatchHandlerModel;
import lombok.Getter;

/**
 * @author durenhao
 * @date 2020/2/21 19:00
 **/
@Getter
public class ListenerConsumerContext {

    private Integer batchSize;

    private BatchHandlerModel batchHandlerModel;

    public ListenerConsumerContext(Integer batchSize, BatchHandlerModel batchHandlerModel) {
        this.batchSize = batchSize;
        this.batchHandlerModel = batchHandlerModel;
    }
}
