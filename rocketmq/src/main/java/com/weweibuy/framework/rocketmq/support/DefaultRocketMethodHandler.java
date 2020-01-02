package com.weweibuy.framework.rocketmq.support;

import com.weweibuy.framework.rocketmq.core.RocketMethodMetadata;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;

import java.util.Map;

/**
 * @author durenhao
 * @date 2019/12/29 23:36
 **/
public class DefaultRocketMethodHandler implements MethodHandler {

    private final MQProducer mqProducer;

    private final RocketMethodMetadata rocketMethodMetadata;

    public DefaultRocketMethodHandler(MQProducer mqProducer, RocketMethodMetadata rocketMethodMetadata) {
        this.mqProducer = mqProducer;
        this.rocketMethodMetadata = rocketMethodMetadata;
    }


    @Override
    public Object invoke(Object[] arg) throws Throwable {
        Message message = buildMsgFromMetadata(arg, rocketMethodMetadata);
        return mqProducer.send(message);
    }

    private Message buildMsgFromMetadata(Object[] arg, RocketMethodMetadata metadata) {
        Integer bodyIndex = metadata.getBodyIndex();
        Map<Integer, MethodParameterProcessor> methodParameterProcessorMap = metadata.getMethodParameterProcessorMap();
        MethodParameterProcessor parameterProcessor = methodParameterProcessorMap.get(bodyIndex);
        Message message = parameterProcessor.process(metadata, new Message(), arg, bodyIndex);

        if (metadata.getTagIndex() == null && StringUtils.isNotBlank(metadata.getTag())) {
            message.setTags(metadata.getTag());
        }

        methodParameterProcessorMap.entrySet().stream()
                .filter(e -> !e.getKey().equals(bodyIndex))
                .forEach(e -> e.getValue().process(metadata, message, arg, e.getKey()));
        return message;
    }


}
