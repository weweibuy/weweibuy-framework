package com.weweibuy.framework.samples.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * @author zhang.suxing
 * @date 2020/10/12 21:43
 **/
@Configuration
public class DistributeConfig {

    @Bean
    @SuppressWarnings("unchecked")
    public RedisScript redisRequestRateLimiterScript() {
        DefaultRedisScript redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(
                new ClassPathResource("META-INF/script/limit.lua")));
        redisScript.setResultType(String.class);
        return redisScript;
    }
}
