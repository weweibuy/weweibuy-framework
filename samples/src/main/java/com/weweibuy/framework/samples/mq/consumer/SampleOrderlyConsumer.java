package com.weweibuy.framework.samples.mq.consumer;

import com.weweibuy.framework.rocketmq.annotation.Payload;
import com.weweibuy.framework.rocketmq.annotation.RocketConsumerHandler;
import com.weweibuy.framework.rocketmq.annotation.RocketListener;
import com.weweibuy.framework.samples.message.SampleUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author durenhao
 * @date 2020/3/20 11:05
 **/
@Slf4j
@Component
@RocketListener(topic = "TEST_SAMPLE_ORDERLY_01", group = "TEST_SAMPLE_ORDERLY_01_C_GROUP", orderly = true)
public class SampleOrderlyConsumer {

    /**
     * 顺序消费
     *
     * @param user
     */
    @RocketConsumerHandler(tags = "QQQ")
    public void onMessage(@Payload SampleUser user) {
        log.info("收到消息: {}, \r\n {} ", user);
    }

}
