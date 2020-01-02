package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.samples.message.SampleUser;
import com.weweibuy.framework.samples.mq.provider.SampleProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author durenhao
 * @date 2019/12/28 20:06
 **/
@Slf4j
@RestController
public class HelloController {

    private final SampleProvider sampleProvider;

    public HelloController(SampleProvider sampleProvider) {
        this.sampleProvider = sampleProvider;
    }

    @GetMapping("/hello")
    public String hello(String msg) {
        SampleUser sampleUser = new SampleUser();
        sampleUser.setUserName(msg);
        sampleUser.setAge(12);
        LocalDateTime of = LocalDateTime.of(1990, 12, 12, 21, 22);
        sampleUser.setBirthday(of);

        SendResult send = sampleProvider.send(sampleUser, "QQQ");
        return "hello";
    }

}
