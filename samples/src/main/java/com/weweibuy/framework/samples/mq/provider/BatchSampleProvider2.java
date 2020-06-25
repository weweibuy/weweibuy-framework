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
@RocketProducer(topic = "TEST_SAMPLE_BATCH_02")
public interface BatchSampleProvider2 {


    @RocketProducerHandler(tag = "BBB", batch = true)
    SendResult sendBatch(Collection<SampleUser> users);


}
