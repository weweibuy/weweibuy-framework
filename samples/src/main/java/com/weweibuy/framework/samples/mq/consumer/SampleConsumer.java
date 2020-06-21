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
@RocketListener(topic = "TEST_SAMPLE_01", group = "TEST_SAMPLE_01_C_GROUP") // 定义一个RocketListener topic  group 支持EL表示式
public class SampleConsumer {

    /**
     * 接受消息, Tag为 QQQ
     *
     * @param user    @Payload 标记消息体
     * @param context
     */
    @RocketConsumerHandler(tags = "QQQ")
    public void onMessage(@Payload SampleUser user, ConsumeConcurrentlyContext context) {
        log.info("收到消息: {} ", user);
    }

    /**
     * @param user      消息体
     * @param tag       @Header 获取消息属性
     * @param headerMap 消息属性
     */
    @RocketConsumerHandler(tags = "AAA")
    public void onMessage2(@Payload SampleUser<SampleDog> user, @Header(MessageConst.PROPERTY_TAGS) String tag, @Header Map<String, String> headerMap) {
        log.info("收到消息: {}", user);
    }

    /**
     * 多个TAG
     *
     * @param user
     */
    @RocketConsumerHandler(tags = "BBB||CCC")
    public void onMessage3(@Payload SampleUser<SampleDog> user) {
        log.info("收到消息: {}", user);
    }

    /**
     * 直接获取原始消息
     *
     * @param messageExt
     */
    @RocketConsumerHandler(tags = "DDD")
    public void onMessage5(MessageExt messageExt) {
        log.info("收到消息: {}", messageExt);
    }

}
