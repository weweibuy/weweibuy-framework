package com.weweibuy.framework.rocketmq.core.consumer;

import com.weweibuy.framework.rocketmq.annotation.Payload;
import com.weweibuy.framework.rocketmq.core.MessageConverter;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.core.MethodParameter;

import java.lang.reflect.ParameterizedType;
import java.util.List;
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
            return messageConverter.fromMessageBody(message.getBody(), parameter);
        } else if (messageObject instanceof List) {
            // 批量消费
            List<MessageExt> messageExtList = (List<MessageExt>) messageObject;

            RocketMethodParameter rocketMethodParameter = (RocketMethodParameter) parameter;
            Class<?> parameterType = parameter.getParameterType();

            // 批量消息用泛型转
            ParameterizedType genericParameterType = (ParameterizedType) parameter.getGenericParameterType();
            Class type = (Class) genericParameterType.getActualTypeArguments()[0];

            return messageExtList.stream()
                    .map(m -> messageConverter.fromMessageBody(m.getBody(), type))
                    .collect(Collectors.toList());
        } else {
            throw new IllegalStateException("消费方法错误");
        }

    }

}
