package com.weweibuy.framework.rocketmq.core.consumer;

import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.core.MethodParameter;

import java.util.List;

/**
 * 参数解析器
 *
 * @author durenhao
 * @date 2020/1/6 23:19
 **/
public interface HandlerMethodArgumentResolver {

    /**
     * 是否支持参数
     *
     * @param parameter
     * @return
     */
    boolean supportsParameter(MethodParameter parameter);

    /**
     * 解析参数
     *
     * @param parameter
     * @param message
     * @return
     */
    Object resolveArgument(MethodParameter parameter, List<MessageExt> message);


}
