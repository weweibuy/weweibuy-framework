package com.weweibuy.framework.samples.mq.provider;

import com.weweibuy.framework.rocketmq.annotation.RocketProvider;
import com.weweibuy.framework.rocketmq.annotation.RocketProviderHandler;

/**
 * @author durenhao
 * @date 2020/6/17 22:33
 **/
@RocketProvider(topic = "SERVER_CHANGE_TOPIC")
public interface lbServiceChangeProvider {

    @RocketProviderHandler
    public void sendServiceChangeMsg(ServerChangeMessage message);

}
