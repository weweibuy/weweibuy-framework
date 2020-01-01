package com.weweibuy.framework.samples.mq.provider;

import com.weweibuy.framework.rocketmq.annotation.RocketProvider;
import com.weweibuy.framework.rocketmq.annotation.RocketProviderHandler;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;

/**
 * @author durenhao
 * @date 2019/12/29 10:26
 **/
@RocketProvider(topic = "${rocket-mq.provider.sample-provider.topic}")
public interface SampleProvider {

    @RocketProviderHandler(tag = "TEST_TAG")
    SendResult send(Object msg);

    @RocketProviderHandler(tag = "TEST_TAG")
    SendResult sendAsync(Object msg, SendCallback sendCallback);

}
