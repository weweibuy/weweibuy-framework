package com.weweibuy.framework.idempotent.redis;

import com.weweibuy.framework.common.core.exception.IdempotentNoLockException;
import com.weweibuy.framework.idempotent.core.support.IdempotentInfo;
import com.weweibuy.framework.idempotent.core.support.IdempotentManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.Assert;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author durenhao
 * @date 2020/6/20 0:03
 **/
public class RedisIdempotentManager implements IdempotentManager {

    private ValueOperations<String, String> valueOperations;

    private RedisIdempotentProperties jdbcIdempotentProperties;

    private StringRedisTemplate stringRedisTemplate;

    private final String pid = ManagementFactory.getRuntimeMXBean().getName();

    public RedisIdempotentManager(StringRedisTemplate stringRedisTemplate,
                                  RedisIdempotentProperties jdbcIdempotentProperties) {
        Assert.notNull(stringRedisTemplate, "stringRedisTemplate 不能为空");
        Assert.notNull(jdbcIdempotentProperties, "jdbcIdempotentProperties 不能为空");
        this.jdbcIdempotentProperties = jdbcIdempotentProperties;
        this.stringRedisTemplate = stringRedisTemplate;
        this.valueOperations = stringRedisTemplate.opsForValue();
    }

    @Override
    public boolean tryLock(IdempotentInfo idempotentInfo) {
        return valueOperations
                .setIfAbsent(generatorKey(idempotentInfo.getKey()), generatorValue(),
                        idempotentInfo.getMaxLockMilli(), TimeUnit.MILLISECONDS);
    }

    @Override
    public Object handlerNoLock(IdempotentInfo idempotentInfo) {
        // Redis 情况下没有获取导锁直接抛出异常?
        throw new IdempotentNoLockException("无法获取到锁,不能进行操作,稍后再试");
    }

    @Override
    public void complete(IdempotentInfo idempotentInfo, Object result) {
        stringRedisTemplate.delete(generatorKey(idempotentInfo.getKey()));
    }

    private String generatorKey(String key) {
        String keyPrefix = jdbcIdempotentProperties.getKeyPrefix();
        if (StringUtils.isNotBlank(keyPrefix)) {
            key = keyPrefix + key;
        }
        return key;
    }

    private String generatorValue() {
        return pid + "_" + Thread.currentThread().getName();
    }
}
