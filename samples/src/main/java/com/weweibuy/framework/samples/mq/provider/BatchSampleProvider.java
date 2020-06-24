package com.weweibuy.framework.samples.mq.provider;

import com.weweibuy.framework.rocketmq.annotation.RocketProducer;
import com.weweibuy.framework.rocketmq.annotation.RocketProducerHandler;
import com.weweibuy.framework.samples.message.SampleUser;
import org.apache.rocketmq.client.producer.SendResult;

import java.util.Collection;

/**
 * @author durenhao
 * @date 2019/12/29 10:26
 **/
@RocketProducer(topic = "${rocket-mq.producer.batch-sample-provider.topic}")
public interface BatchSampleProvider {


    @RocketProducerHandler(tag = "CCC", batch = true)
    SendResult sendBatch(Collection<SampleUser> users);

    @RocketProducerHandler(tag = "BBB", batch = true)
    SendResult sendBatch2(Collection<SampleUser> users);


}
