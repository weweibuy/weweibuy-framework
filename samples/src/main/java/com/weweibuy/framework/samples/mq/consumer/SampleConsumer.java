package com.weweibuy.framework.samples.mq.consumer;

import com.weweibuy.framework.rocketmq.annotation.Header;
import com.weweibuy.framework.rocketmq.annotation.Payload;
import com.weweibuy.framework.rocketmq.annotation.RocketConsumerHandler;
import com.weweibuy.framework.rocketmq.annotation.RocketListener;
import com.weweibuy.framework.samples.message.SampleDog;
import com.weweibuy.framework.samples.message.SampleUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author durenhao
 * @date 2019/12/29 10:26
 **/
@Slf4j
@Component
@RocketListener(topic = "TEST_SAMPLE_01", group = "TEST_SAMPLE_01_C_GROUP")
public class SampleConsumer {

    //    @RocketConsumerHandler(tags = "QQQ")
    public void onMessage(@Payload SampleUser user, ConsumeConcurrentlyContext context) {
        log.info("收到消息: {}, \r\n {} ", user, context);
    }

    @RocketConsumerHandler(tags = "AAA")
    public void onMessage2(@Payload SampleUser<SampleDog> user, @Header(MessageConst.PROPERTY_TAGS) String tag, @Header Map<String, String> headerMap) {
        log.info("收到消息: {}", user);
    }

    @RocketConsumerHandler(tags = "BBB||CCC")
    public void onMessage3(@Payload SampleUser<SampleDog> user) {
        log.info("收到消息: {}", user);
    }


    @RocketConsumerHandler(tags = "DDD")
    public void onMessage5(MessageExt messageExt) {
        log.info("收到消息: {}", messageExt);
    }


}
