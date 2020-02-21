package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.samples.message.SampleDog;
import com.weweibuy.framework.samples.message.SampleUser;
import com.weweibuy.framework.samples.mq.provider.BatchSampleProvider;
import com.weweibuy.framework.samples.mq.provider.SampleProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author durenhao
 * @date 2019/12/28 20:06
 **/
@Slf4j
@RestController
public class HelloController {

    private SampleProvider sampleProvider;

    private BatchSampleProvider batchSampleProvider;

    public HelloController(SampleProvider sampleProvider, BatchSampleProvider batchSampleProvider) {
        this.sampleProvider = sampleProvider;
        this.batchSampleProvider = batchSampleProvider;
    }

    @GetMapping("/hello")
    public String hello(String msg) {
        SampleUser user = user(msg);
        user.setSampleDog(dog());
        SendResult send = sampleProvider.send(user, "AAA", UUID.randomUUID().toString());

        return "hello";
    }

    @GetMapping("/hello-async")
    public String helloAsync(String msg) {
        SampleUser user = user(msg);

        sampleProvider.sendAsync(user, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("异步发送成功");
            }

            @Override
            public void onException(Throwable e) {
                log.info("异步发送失败");
            }
        }, UUID.randomUUID().toString());

        return "hello";
    }

    @GetMapping("/hello-batch")
    public String helloBatch(String msg) {
        SampleUser user = user(msg);
        user.setSampleDog(dog());
        SampleUser user1 = user(msg + "QQQ");
        user1.setSampleDog(dog());
        ArrayList<SampleUser> list = new ArrayList<>();
        list.add(user);
        list.add(user1);
        list.add(user1);
        list.add(user1);
        list.add(user1);
        list.add(user1);
        batchSampleProvider.sendBatch(list);
        return "hello";
    }

    private SampleUser user(String name) {
        SampleUser sampleUser = new SampleUser();
        sampleUser.setUserName(name);
        sampleUser.setAge(12);
        LocalDateTime of = LocalDateTime.of(1990, 12, 12, 21, 22);
        sampleUser.setBirthday(of);
        return sampleUser;
    }

    private SampleDog dog() {
        SampleDog sampleDog = new SampleDog();
        sampleDog.setDogName("dog tom");
        sampleDog.setDogAge(11);
        return sampleDog;
    }

}
