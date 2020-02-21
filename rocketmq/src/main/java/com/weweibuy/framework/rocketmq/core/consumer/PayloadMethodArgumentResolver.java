package com.weweibuy.framework.rocketmq.core.consumer;

import com.weweibuy.framework.rocketmq.annotation.Payload;
import com.weweibuy.framework.rocketmq.core.MessageConverter;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.core.MethodParameter;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author durenhao
 * @date 2020/1/9 10:57
 **/
public class PayloadMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final MessageConverter messageConverter;

    public PayloadMethodArgumentResolver(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Payload.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Object messageObject) {

        if (messageObject instanceof MessageExt) {
            MessageExt message = (MessageExt) messageObject;
            return messageConverter.fromMessageBody(message.getBody(), parameter, false);
        } else if (messageObject instanceof Collection) {
            // 批量消费
            Collection<MessageExt> messageExtList = (Collection<MessageExt>) messageObject;

            return messageExtList.stream()
                    .map(m -> messageConverter.fromMessageBody(m.getBody(), parameter, true))
                    .collect(Collectors.toList());
        } else {
            throw new IllegalStateException("消费方法错误");
        }

    }

}
