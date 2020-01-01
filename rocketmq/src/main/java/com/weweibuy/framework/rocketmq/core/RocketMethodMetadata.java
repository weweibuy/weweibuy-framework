package com.weweibuy.framework.rocketmq.core;

import com.weweibuy.framework.rocketmq.support.MessageKeyGenerator;
import com.weweibuy.framework.rocketmq.support.MethodParameterProcessor;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 方法元数据
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

    private Map<Integer, String> headerIndexName;

    private Boolean orderly;

    private Boolean oneWay;

    private Boolean batch;

    private Integer timeout;

    private String tag;

    private String keyExpression;

    private MessageKeyGenerator messageKeyGenerator;

    private Map<Integer, MethodParameterProcessor> methodParameterProcessorMap = new HashMap<>();
}
