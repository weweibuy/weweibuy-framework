package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.samples.mq.provider.SampleProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
        sampleProvider.send(msg);
        return "hello";
    }

}
