package com.weweibuy.framework.rocketmq.core.consumer;

import com.weweibuy.framework.rocketmq.annotation.Header;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.core.MethodParameter;

import java.util.List;

/**
 * header 注解解析器
 *
 * @author durenhao
 * @date 2020/1/9 11:37
 **/
public class HeaderMethodArgumentResolver implements HandlerMethodArgumentResolver {


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasMethodAnnotation(Header.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, List<MessageExt> message) throws Exception {
        return null;
    }
}
