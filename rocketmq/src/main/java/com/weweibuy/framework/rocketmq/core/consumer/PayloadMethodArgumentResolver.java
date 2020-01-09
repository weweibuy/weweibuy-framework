/*
 * All rights Reserved, Designed By baowei
 *
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.weweibuy.framework.rocketmq.core.consumer;

import com.weweibuy.framework.rocketmq.annotation.Payload;
import com.weweibuy.framework.rocketmq.core.MessageConverter;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.core.MethodParameter;

import java.util.List;

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
    public Object resolveArgument(MethodParameter parameter, List<MessageExt> message) throws Exception {
        if (message.size() == 1) {
            return messageConverter.fromMessageBody(message.get(0).getBody(), parameter);
        }
        return null;
    }
}
