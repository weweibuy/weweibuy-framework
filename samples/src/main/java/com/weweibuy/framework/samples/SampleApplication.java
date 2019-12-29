package com.weweibuy.framework.samples;

import com.weweibuy.framework.rocketmq.annotation.EnableRocketProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author durenhao
 * @date 2019/12/28 20:02
 **/
@SpringBootApplication
@EnableRocketProvider
public class SampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

}
