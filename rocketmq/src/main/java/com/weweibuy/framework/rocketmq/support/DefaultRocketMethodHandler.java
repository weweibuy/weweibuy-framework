package com.weweibuy.framework.rocketmq.support;

import com.weweibuy.framework.rocketmq.core.RocketMethodMetadata;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.apache.rocketmq.common.message.Message;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author durenhao
 * @date 2019/12/29 23:36
 **/
@Slf4j
public class DefaultRocketMethodHandler implements MethodHandler {

    private final MQProducer mqProducer;

    private final RocketMethodMetadata metadata;

    private final MessageQueueSelector messageQueueSelector = new SelectMessageQueueByHash();

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
            SendResult send = mqProducer.send(messages, metadata.getTimeout());
            log.info("发送MQ消息 Topic:【{}】, 结果: {}", metadata.getTopic(), send.getSendStatus());
        }

        Message message = buildMsgFromMetadata(args);

        if (metadata.getOneWay() && metadata.getOrderly()) {
            mqProducer.sendOneway(message, messageQueueSelector, message.getKeys());
            return null;
        } else if (metadata.getOneWay() && !metadata.getOrderly()) {
            mqProducer.sendOneway(message);
            return null;
        } else if (metadata.getOrderly() && metadata.getAsyncIndex() != null) {
            mqProducer.send(message, messageQueueSelector, message.getKeys(), (SendCallback) args[metadata.getAsyncIndex()]);
            return null;
        } else if (metadata.getOrderly() && metadata.getAsyncIndex() == null) {
            SendResult send = mqProducer.send(message, messageQueueSelector, message.getKeys());
            log.info("MQ消息 Topic:【{}】, Tag:【{}】, Key:【{}】, 发送结果: {}",
                    message.getTopic(), message.getTags(), message.getKeys(), send.getSendStatus());
            return send;
        } else if (metadata.getAsyncIndex() != null) {
            mqProducer.send(message, (SendCallback) args[metadata.getAsyncIndex()], metadata.getTimeout());
            return null;
        } else {
            SendResult send = mqProducer.send(message, metadata.getTimeout());
            log.info("MQ消息 Topic:【{}】, Tag:【{}】, Key:【{}】, 发送结果: {}",
                    message.getTopic(), message.getTags(), message.getKeys(), send.getSendStatus());
            return send;
        }

    }

    private Message buildMsgFromMetadata(Object[] arg) {
        Integer bodyIndex = metadata.getBodyIndex();
        Map<Integer, MethodParameterProcessor> methodParameterProcessorMap = metadata.getMethodParameterProcessorMap();
        MethodParameterProcessor parameterProcessor = methodParameterProcessorMap.get(bodyIndex);
        Message message = parameterProcessor.process(metadata, new Message(), arg, bodyIndex);

        if (metadata.getTagIndex() == null && StringUtils.isNotBlank(metadata.getTag())) {
            message.setTags(metadata.getTag());
        }

        if (metadata.getKeyIndex() == null && StringUtils.isNotBlank(metadata.getKeyExpression())) {
            message.setKeys(metadata.getMessageKeyGenerator().generatorKey(metadata, arg));
        }

        methodParameterProcessorMap.entrySet().stream()
                .filter(e -> !e.getKey().equals(bodyIndex))
                .forEach(e -> e.getValue().process(metadata, message, arg, e.getKey()));
        log.info("发送MQ消息 Topic:【{}】, Tag:【{}】, Key:【{}】, 消息体: {}", message.getTopic(), message.getTags(), message.getKeys(), new String(message.getBody()));
        return message;
    }


}
