package com.weweibuy.framework.common.lc.config;

import com.weweibuy.framework.common.lc.mq.LocalCacheEvictConsumer;
import com.weweibuy.framework.rocketmq.config.ConsumerConfig;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MQ 刷新本地缓存配置
 *
 * @author durenhao
 * @date 2020/12/6 12:20
 **/
@Configuration
@AutoConfigureAfter(CaffeineCacheManager.class)
public class LocalCacheEvictMqConfig {

    @Bean
    @ConditionalOnBean({CacheManager.class, ConsumerConfig.class})
    public LocalCacheEvictConsumer localCacheEvictConsumer() {
        return new LocalCacheEvictConsumer();
    }
}
