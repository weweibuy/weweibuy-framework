package com.weweibuy.framework.rocketmq.support;

import com.weweibuy.framework.rocketmq.core.RocketMethodMetadata;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author durenhao
 * @date 2019/12/29 23:36
 **/
public class DefaultRocketMethodHandler implements MethodHandler {

    private final MQProducer mqProducer;

    private final RocketMethodMetadata metadata;

    public DefaultRocketMethodHandler(MQProducer mqProducer, RocketMethodMetadata rocketMethodMetadata) {
        this.mqProducer = mqProducer;
        this.metadata = rocketMethodMetadata;
    }


    @Override
    public Object invoke(Object[] args) throws Throwable {
        Boolean batch = metadata.getBatch();
        if (batch) {
            Integer bodyIndex = metadata.getBodyIndex();
            Collection collection = (Collection) args[metadata.getBodyIndex()];
            Collection<Message> messages = (Collection<Message>) collection.stream()
                    .peek(a -> args[bodyIndex] = a)
                    .map(a -> buildMsgFromMetadata(args))
                    .collect(Collectors.toList());
            return mqProducer.send(collection, metadata.getTimeout());
        }

        Message message = buildMsgFromMetadata(args);
        return mqProducer.send(message, metadata.getTimeout());
    }

    private Message buildMsgFromMetadata(Object[] arg) {
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
