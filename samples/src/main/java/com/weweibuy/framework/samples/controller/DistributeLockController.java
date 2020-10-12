package com.weweibuy.framework.samples.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.UUID;

/**
 * @author zhang.suxing
 * @date 2020/10/12 19:35
 **/
@RestController
@RequestMapping("/lock/distribute")
public class DistributeLockController {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedisScript<String> redisScript;

    private static final String LOCK_SUCCESS = "true";

    private static final String EXPIRE_PERIOD = "60";

    @GetMapping("/single")
    public ResponseEntity testLock(@RequestParam String key) {
        return ResponseEntity.ok(tryGetToken(key));

    }

    private boolean tryGetToken(String localKey) {
        String reqId = UUID.randomUUID().toString();
        //设置脚本
        return LOCK_SUCCESS.equalsIgnoreCase(redisTemplate.execute(redisScript, Collections.singletonList(localKey), reqId, EXPIRE_PERIOD));
    }

}
