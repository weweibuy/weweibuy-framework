package com.weweibuy.framework.samples.mq.provider;

import com.weweibuy.framework.rocketmq.annotation.Key;
import com.weweibuy.framework.rocketmq.annotation.RocketProvider;
import com.weweibuy.framework.rocketmq.annotation.RocketProviderHandler;
import com.weweibuy.framework.rocketmq.annotation.Tag;
import com.weweibuy.framework.samples.message.SampleUser;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;

import java.util.Collection;

/**
 * @author durenhao
 * @date 2019/12/29 10:26
 **/
@RocketProvider(topic = "${rocket-mq.provider.sample-provider.topic}")
public interface SampleProvider {

    @RocketProviderHandler(tag = "${rocket-mq.provider.sample-provider.tag}")
    SendResult send(SampleUser user, @Tag String s, @Key String key);

    @RocketProviderHandler(tag = "TEST_TAG",  orderly = true)
    SendResult sendAsync(SampleUser user, SendCallback sendCallback, @Key String key);

    @RocketProviderHandler(tag = "AAA", batch = true)
    SendResult sendBatch(Collection<SampleUser> users);


}
