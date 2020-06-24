package com.weweibuy.framework.rocketmq.core.consumer;

import com.weweibuy.framework.rocketmq.annotation.Property;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.core.MethodParameter;

/**
 * header 注解解析器
 *
 * @author durenhao
 * @date 2020/1/9 11:37
 **/
@Slf4j
public class HeaderMethodArgumentResolver implements HandlerMethodArgumentResolver {


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Property.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Object messageObject) {
        if (messageObject instanceof MessageExt) {
            MessageExt messageExt = (MessageExt) messageObject;
            Property header = parameter.getParameterAnnotation(Property.class);
            if (StringUtils.isNotBlank(header.value())) {
                return messageExt.getProperty(header.value());
            } else {
                return messageExt.getProperties();
            }
        }
        log.warn("批量消息不支持 @Header 形式");
        return null;
    }

}
