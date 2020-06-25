package com.weweibuy.framework.samples.mq.consumer;

import com.weweibuy.framework.rocketmq.annotation.BatchHandlerModel;
import com.weweibuy.framework.rocketmq.annotation.Payload;
import com.weweibuy.framework.rocketmq.annotation.RocketConsumerHandler;
import com.weweibuy.framework.rocketmq.annotation.RocketListener;
import com.weweibuy.framework.samples.message.SampleDog;
import com.weweibuy.framework.samples.message.SampleUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 批量消息迭代消费
 *
 * @author durenhao
 * @date 2019/12/29 10:26
 **/
@Slf4j
@Component
@RocketListener(topic = "TEST_SAMPLE_BATCH_02", group = "TEST_SAMPLE_BATCH_02_C_GROUP", consumeMessageBatchMaxSize = 50)
public class BatchForEachSampleConsumer {


    @RocketConsumerHandler(tags = "AAA||BBB||CCC", batchHandlerModel = BatchHandlerModel.FOREACH)
    public void onMessage(@Payload SampleUser<SampleDog> user) {
        log.info("收到消息: {}", user);
    }


}
