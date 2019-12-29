package com.weweibuy.framework.rocketmq.support;

import com.weweibuy.framework.rocketmq.core.MessageConverter;
import org.apache.rocketmq.client.producer.MQProducer;

/**
 * @author durenhao
 * @date 2019/12/29 23:36
 **/
public class DefaultRocketMethodHandler implements MethodHandler {

    private final MQProducer mqProducer;

    private final MessageConverter messageConverter;

    public DefaultRocketMethodHandler(MQProducer mqProducer,
                                      MessageConverter messageConverter) {
        this.mqProducer = mqProducer;
        this.messageConverter = messageConverter;
    }


    @Override
    public Object invoke(Object[] arg) throws Throwable {
        return null;
    }
}
