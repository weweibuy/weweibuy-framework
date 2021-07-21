package com.weweibuy.framework.common.lc.mq;

import com.weweibuy.framework.common.lc.mq.message.LocalCacheEvictMessage;
import com.weweibuy.framework.rocketmq.annotation.RocketProducer;
import com.weweibuy.framework.rocketmq.annotation.RocketProducerHandler;
import org.apache.rocketmq.client.producer.SendResult;

/**
 * 发送失效本地缓存 MQ 消息
 *
 * @author durenhao
 * @date 2020/12/6 11:31
 **/
@RocketProducer(topic = "${common.local-cache.mq-evict.topic}")
public interface LocalCacheEvictProducer {

    @RocketProducerHandler(tag = "${common.local-cache.mq-evict.tag}")
    SendResult send(LocalCacheEvictMessage evictMessage);

}
