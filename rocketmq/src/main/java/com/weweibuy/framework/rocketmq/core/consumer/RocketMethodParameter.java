package com.weweibuy.framework.rocketmq.core.consumer;

import org.springframework.core.annotation.SynthesizingMethodParameter;

import java.lang.reflect.Method;

/**
 * @author durenhao
 * @date 2020/1/18 17:36
 **/
public class RocketMethodParameter extends SynthesizingMethodParameter {

    private Integer batchSize;

    public RocketMethodParameter(Method method, int parameterIndex, Integer batchSize) {
        super(method, parameterIndex);
        this.batchSize = batchSize;
    }

    public Integer getBatchSize() {
        return batchSize;
    }
}
