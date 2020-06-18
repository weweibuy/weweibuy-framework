package com.weweibuy.framework.samples.config;

import com.weweibuy.framework.rocketmq.core.consumer.RocketListenerErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
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
        if (messageObject instanceof MessageExt) {
            MessageExt messageExt = (MessageExt) messageObject;
            log.warn("消费MQ消息: Topic:【{}】, Tag:【{}】, Key:【{}】 Body: 【{}】, 出现异常: ",
                    messageExt.getTopic(),
                    messageExt.getTags(),
                    messageExt.getKeys(),
                    new String(messageExt.getBody()),
                    e);
        }
        log.warn("消费MQ消息: {}, 出现异常: ", messageObject, e);

        return true;
    }
}
