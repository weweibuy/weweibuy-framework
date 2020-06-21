package com.weweibuy.framework.idempotent.redis.config;

import com.weweibuy.framework.idempotent.core.config.IdempotentConfig;
import com.weweibuy.framework.idempotent.core.support.IdempotentManager;
import com.weweibuy.framework.idempotent.redis.RedisIdempotentManager;
import com.weweibuy.framework.idempotent.redis.RedisIdempotentProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author durenhao
 * @date 2020/6/20 13:17
 **/
@Configuration
@EnableConfigurationProperties(value = RedisIdempotentProperties.class)
@ConditionalOnBean(IdempotentConfig.class)
@ConditionalOnProperty(prefix = "idempotent.redis", name = "enable", havingValue = "true", matchIfMissing = true)
public class RedisIdempotentConfig {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisIdempotentProperties redisIdempotentProperties;

    @Bean
    public IdempotentManager redisIdempotentManager() {
        return new RedisIdempotentManager(redisTemplate, redisIdempotentProperties);
    }

}
