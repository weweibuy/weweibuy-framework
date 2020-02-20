package com.weweibuy.framework.rocketmq.support;

import com.weweibuy.framework.rocketmq.core.provider.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author durenhao
 * @date 2019/12/29 23:36
 **/
@Slf4j
public class DefaultRocketMethodHandler implements MethodHandler {

    private final RocketMethodMetadata metadata;

    private final MessageQueueSelector messageQueueSelector;

    private final List<MessageSendFilter> messageSendFilterList;

    private final MQProducer mqProducer;

    public DefaultRocketMethodHandler(RocketMethodMetadata metadata, MessageQueueSelector messageQueueSelector,
                                      List<MessageSendFilter> messageSendFilterList, MQProducer mqProducer) {
        this.metadata = metadata;
        this.messageQueueSelector = messageQueueSelector;
        this.messageSendFilterList = messageSendFilterList;
        this.mqProducer = mqProducer;
    }

    @Override
    public Object invoke(Object[] args) throws Throwable {

        MessageSendFilterEnter chain = new MessageSendFilterEnter(messageSendFilterList);

        MessageSendContext context = new MessageSendContext(metadata, messageQueueSelector, args);
        Object message = null;
        if (context.isBatch()) {
            Integer bodyIndex = metadata.getBodyIndex();
            Collection collection = (Collection) args[metadata.getBodyIndex()];
            message = (Collection<Message>) collection.stream()
                    .peek(a -> args[bodyIndex] = a)
                    .map(a -> buildMsgFromMetadata(args))
                    .collect(Collectors.toList());
        } else {
            message = buildMsgFromMetadata(args);
        }

        return chain.doFilter(new MessageSendContext(metadata, messageQueueSelector, args), message, mqProducer);

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
