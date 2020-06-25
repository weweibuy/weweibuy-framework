package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.common.core.utils.IdWorker;
import com.weweibuy.framework.samples.message.SampleDog;
import com.weweibuy.framework.samples.message.SampleUser;
import com.weweibuy.framework.samples.model.dto.CommonDataJsonResponse;
import com.weweibuy.framework.samples.mq.provider.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private lbServiceChangeProvider lbServiceChangeProvider;

    private BatchSampleProvider2 batchSampleProvider2;

    public HelloController(SampleProvider sampleProvider, BatchSampleProvider batchSampleProvider, com.weweibuy.framework.samples.mq.provider.lbServiceChangeProvider lbServiceChangeProvider, BatchSampleProvider2 batchSampleProvider2) {
        this.sampleProvider = sampleProvider;
        this.batchSampleProvider = batchSampleProvider;
        this.lbServiceChangeProvider = lbServiceChangeProvider;
        this.batchSampleProvider2 = batchSampleProvider2;
    }

    @GetMapping("/hello")
    public Object hello(String msg, String tag, String key) {
        SampleUser user = user(msg);
        user.setSampleDog(dog());
        SendResult send = sampleProvider.send(user, tag, key);
        return CommonDataJsonResponse.success();
    }

    @GetMapping("/hello-oneway")
    public Object helloOneWay(String msg, String tag, String key) {
        SampleUser user = user(msg);
        user.setSampleDog(dog());
        sampleProvider.sendOneWay(user);
        return CommonDataJsonResponse.success();
    }

    @GetMapping("/hello-orderly")
    public Object helloOrderly(String msg, String tag, String key) {
        SampleUser user = user(msg);
        user.setSampleDog(dog());
        sampleProvider.sendOrder(user, IdWorker.nextStringId());
        return CommonDataJsonResponse.success();
    }

    @GetMapping("/hello-header")
    public Object helloHeader(String msg, String tag) {
        SampleUser user = user(msg);
        user.setSampleDog(dog());
        SendResult send = sampleProvider.sendHeader(user, tag);
        return CommonDataJsonResponse.success();
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


    @GetMapping("/hello-batch-foreach")
    public String helloBatchForeach(String msg) {
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
        batchSampleProvider2.sendBatch(list);
        return "hello";
    }

    private static SampleUser user(String name) {
        SampleUser sampleUser = new SampleUser();
        sampleUser.setUserName(name);
        sampleUser.setAge(12);
//        LocalDateTime of = LocalDateTime.of(1990, 12, 12, 21, 22);
//        sampleUser.setBirthday(of);
        return sampleUser;
    }

    private static SampleDog dog() {
        SampleDog sampleDog = new SampleDog();
        sampleDog.setDogName("dog tom");
        sampleDog.setDogAge(11);
        return sampleDog;
    }


    @GetMapping("/change")
    public String send(String name, String id){
        ServerChangeMessage message = new ServerChangeMessage();
        message.setName(name);
        message.setInstanceId(id);
        lbServiceChangeProvider.sendServiceChangeMsg(message);
        return "success";
    }

}
