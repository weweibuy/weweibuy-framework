package com.weweibuy.framework.samples.mq.consumer;

import com.weweibuy.framework.rocketmq.annotation.BatchForEachConsumerFailPolicy;
import com.weweibuy.framework.rocketmq.annotation.Payload;
import com.weweibuy.framework.rocketmq.annotation.RocketConsumerHandler;
import com.weweibuy.framework.rocketmq.annotation.RocketListener;
import com.weweibuy.framework.samples.message.SampleDog;
import com.weweibuy.framework.samples.message.SampleUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 批量消息迭代消费
 *
 * @author durenhao
 * @date 2019/12/29 10:26
 **/
@Slf4j
@Component
@RocketListener(topic = "TEST_SAMPLE_BATCH_02", group = "TEST_SAMPLE_BATCH_02_C_GROUP", consumeMessageBatchMaxSize = 50,
        foreachFailPolicy = BatchForEachConsumerFailPolicy.CONTINUE_OTHERS)
public class BatchForEachSampleConsumer {


    @RocketConsumerHandler(tags = "AAA||BBB||CCC")
    public void onMessage(@Payload SampleUser<SampleDog> user) {
        log.info("收到消息: {}", user);
    }

    @RocketConsumerHandler(tags = "DDD")
    public void onMessage2(@Payload SampleUser<SampleDog> user, MessageExt messageExt, List<MessageExt> messageExtList) {
        log.info("收到消息: {}", user);
    }

}
