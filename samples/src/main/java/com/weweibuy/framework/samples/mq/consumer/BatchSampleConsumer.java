package com.weweibuy.framework.samples.mq.consumer;

import com.weweibuy.framework.rocketmq.annotation.*;
import com.weweibuy.framework.samples.message.SampleDog;
import com.weweibuy.framework.samples.message.SampleUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author durenhao
 * @date 2019/12/29 10:26
 **/
@Slf4j
@Component
@RocketListener(topic = "TEST_BATCH_SAMPLE_01", group = "TEST_BATCH_SAMPLE_01_C_GROUP", consumeMessageBatchMaxSize = 50)
public class BatchSampleConsumer {


    @RocketConsumerHandler(tags = "AAA", batchHandlerModel = BatchHandlerModel.TOGETHER)
    public void onMessage(@Payload Collection<SampleUser<SampleDog>> user) {
        log.info("收到消息: {}", user);
    }


}
