package com.weweibuy.framework.samples.mq.provider;

import com.weweibuy.framework.rocketmq.annotation.RocketProducer;
import com.weweibuy.framework.rocketmq.annotation.RocketProducerHandler;

/**
 * @author durenhao
 * @date 2020/6/17 22:33
 **/
@RocketProducer(topic = "SERVER_CHANGE_TOPIC")
public interface lbServiceChangeProvider {

    @RocketProducerHandler
    public void sendServiceChangeMsg(ServerChangeMessage message);

}
