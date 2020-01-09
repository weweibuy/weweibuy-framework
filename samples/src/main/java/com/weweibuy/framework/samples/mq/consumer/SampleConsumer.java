package com.weweibuy.framework.samples.mq.consumer;

import com.weweibuy.framework.rocketmq.annotation.Payload;
import com.weweibuy.framework.rocketmq.annotation.RocketConsumerHandler;
import com.weweibuy.framework.rocketmq.annotation.RocketListener;
import com.weweibuy.framework.samples.message.SampleUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * @author durenhao
 * @date 2019/12/29 10:26
 **/
@Slf4j
@Component
@RocketListener(topic = "TEST_SAMPLE_01", group = "TEST_SAMPLE_01_C_GROUP", consumeMessageBatchMaxSize = 10)
public class SampleConsumer {

    @RocketConsumerHandler(tags = "QQQ")
    public void onMessage(@Payload Collection<SampleUser> user) {
        log.info("收到消息: {}", user);
    }

    @RocketConsumerHandler(tags = "AAA")
    public void onMessage2(@Payload Collection<SampleUser> user) {
        log.info("收到消息: {}", user);
    }

    @RocketConsumerHandler
    public void onMessage3(@Payload Collection<SampleUser> user) {
        log.info("收到消息: {}", user);
    }

    @RocketConsumerHandler
    public void onMessage4(@Payload List<MessageExt> messageExt) {
        log.info("收到消息: {}", messageExt);
    }


}
