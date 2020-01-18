package com.weweibuy.framework.rocketmq.core.consumer;

import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.core.MethodParameter;

/**
 * @author durenhao
 * @date 2020/1/18 22:27
 **/
public class RocketDefaultNamedValueMethodArgumentResolver implements HandlerMethodArgumentResolver {


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotations() && !parameter.getParameterType().isInstance(Message.class)) {
            return true;
        }
        return false;
    }


    @Override
    public Object resolveArgument(MethodParameter parameter, Object messageObject) {
        if (messageObject instanceof MessageExt) {
            MessageExt messageExt = (MessageExt) messageObject;
            String name = parameter.getParameter().getName();
        }

        return null;
    }


}
