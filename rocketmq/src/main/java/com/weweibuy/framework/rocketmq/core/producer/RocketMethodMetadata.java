package com.weweibuy.framework.rocketmq.core.producer;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 生产者方法元数据
 *
 * @author durenhao
 * @date 2019/12/30 20:51
 **/
@Data
public class RocketMethodMetadata {

    private String topic;

    private Method method;

    private Integer bodyIndex;

    private Integer tagIndex;

    private Integer keyIndex;

    private Integer headerIndex;

    private Integer asyncIndex;

    private Map<Integer, String> headerIndexName;

    private Boolean orderly;

    private Boolean oneWay;

    private Boolean batch;

    private Integer timeout;

    private String tag;

    private Map<Integer, MethodParameterProcessor> methodParameterProcessorMap = new HashMap<>();
}
