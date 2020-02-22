package com.weweibuy.framework.samples.mq.provider;

import com.weweibuy.framework.rocketmq.annotation.RocketProvider;
import com.weweibuy.framework.rocketmq.annotation.RocketProviderHandler;
import com.weweibuy.framework.samples.message.SampleUser;
import org.apache.rocketmq.client.producer.SendResult;

import java.util.Collection;

/**
 * @author durenhao
 * @date 2019/12/29 10:26
 **/
@RocketProvider(topic = "${rocket-mq.provider.batch-sample-provider.topic}")
public interface BatchSampleProvider {


    @RocketProviderHandler(tag = "CCC", batch = true)
    SendResult sendBatch(Collection<SampleUser> users);

    @RocketProviderHandler(tag = "BBB", batch = true)
    SendResult sendBatch2(Collection<SampleUser> users);


}
