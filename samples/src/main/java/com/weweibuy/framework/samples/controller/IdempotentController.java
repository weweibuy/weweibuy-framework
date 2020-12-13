package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.common.core.model.dto.CommonDataResponse;
import com.weweibuy.framework.samples.idempotent.IdempotentService;
import com.weweibuy.framework.samples.model.User;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author durenhao
 * @date 2020/6/20 16:09
 **/
@RestController
@RequestMapping("/idempotent")
public class IdempotentController {

    private final IdempotentService idempotentService;

    private final StringRedisTemplate redisTemplate;

    public IdempotentController(IdempotentService idempotentService, StringRedisTemplate redisTemplate) {
        this.idempotentService = idempotentService;
        this.redisTemplate = redisTemplate;
    }

    @RequestMapping("/jdbc1")
    public Object jdbc() {
        idempotentService.doBiz();
        return CommonDataResponse.success();
    }

    @RequestMapping("/jdbc2")
    public Object jdbc2(String name) {
        User user = new User();
        user.setName(name);
        user.setAge(12);
        return idempotentService.doBiz2(user);
    }

    @RequestMapping("/jdbc3")
    @Transactional(rollbackFor = Exception.class)
    public Object jdbc3() {
        User user = new User();
        user.setName("tom");
        user.setAge(12);
        return idempotentService.doBiz3(user);
    }


    @RequestMapping("/redis1")
    public Object redis1() throws InterruptedException {
        User user = new User();
        user.setName("tom");
        user.setAge(12);
        return idempotentService.doBiz4(user);
    }

}
