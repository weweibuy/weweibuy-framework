package com.weweibuy.framework.rocketmq.support;

import com.weweibuy.framework.rocketmq.core.MessageConverter;
import com.weweibuy.framework.rocketmq.core.provider.MethodParameterProcessor;
import com.weweibuy.framework.rocketmq.core.provider.RocketMethodMetadata;
import org.apache.rocketmq.common.message.Message;
import org.springframework.util.Assert;

/**
 * 消息体体处理
 *
 * @author durenhao
 * @date 2020/1/1 16:15
 **/
public class MessageBodyParameterProcessor implements MethodParameterProcessor {

    private final MessageConverter messageConverter;

    public MessageBodyParameterProcessor(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    @Override
    public RocketMethodMetadata buildMetadata(RocketMethodMetadata methodMetadata, Class<?> parameterType, int argIndex) {
        Assert.isNull(methodMetadata.getBodyIndex(), "方法: " + methodMetadata.getMethod().getDeclaringClass().getSimpleName() + "."
                + methodMetadata.getMethod().getName() + " 中有多个可以成为消息体的参数");


        methodMetadata.getMethodParameterProcessorMap()
                .put(argIndex, this);

        methodMetadata.setBodyIndex(argIndex);
        return methodMetadata;
    }

    @Override
    public Message process(RocketMethodMetadata methodMetadata, Message message, Object[] args, int index) {
        Object arg = args[index];
        if (arg instanceof Message) {
            // 参数本身就是 Message 不做处理
            return (Message) arg;
        }
        message.setTopic(methodMetadata.getTopic());
        message.setBody(messageConverter.toMessageBody(arg));
        return message;
    }

}
