package com.weweibuy.framework.samples.config;

import com.weweibuy.framework.rocketmq.core.consumer.RocketListenerErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author durenhao
 * @date 2020/2/22 18:56
 **/
@Slf4j
@Component
public class RocketMqConsumerErrorHandler implements RocketListenerErrorHandler {


    @Override
    public boolean handlerException(Exception e, Object messageObject, Boolean orderly) {
        log.warn("消费MQ消息: {}, 出现异常: {}", messageObject, e.getMessage());

        return true;
    }
}
