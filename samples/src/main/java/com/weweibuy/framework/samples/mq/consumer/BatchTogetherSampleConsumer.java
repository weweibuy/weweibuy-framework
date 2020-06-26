package com.weweibuy.framework.samples.mq.consumer;

import com.weweibuy.framework.rocketmq.annotation.BatchHandlerModel;
import com.weweibuy.framework.rocketmq.annotation.Payload;
import com.weweibuy.framework.rocketmq.annotation.RocketConsumerHandler;
import com.weweibuy.framework.rocketmq.annotation.RocketListener;
import com.weweibuy.framework.samples.message.SampleDog;
import com.weweibuy.framework.samples.message.SampleUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 批量消息一起消费
 *
 * @author durenhao
 * @date 2019/12/29 10:26
 **/
@Slf4j
@Component
@RocketListener(topic = "${rocket-mq.producer.batch-sample-provider.topic}", group = "TEST_BATCH_SAMPLE_01_C_GROUP",
        consumeMessageBatchMaxSize = 50, batchHandlerModel = BatchHandlerModel.TOGETHER)
public class BatchTogetherSampleConsumer {


    @RocketConsumerHandler(tags = "AAA||BBB||CCC")
    public void onMessage(@Payload Collection<SampleUser<SampleDog>> user) {
        log.info("收到消息:  {}, userName: {}", user, user.iterator().next().getUserName());
    }


}
