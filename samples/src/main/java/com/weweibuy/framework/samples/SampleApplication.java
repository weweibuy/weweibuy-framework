package com.weweibuy.framework.samples;

import com.weweibuy.framework.compensate.annotation.EnableCompensate;
import com.weweibuy.framework.rocketmq.annotation.EnableRocketConsumer;
import com.weweibuy.framework.rocketmq.annotation.EnableRocketProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author durenhao
 * @date 2019/12/28 20:02
 **/
@SpringBootApplication
@EnableRocketProvider
@EnableCaching
@EnableRocketConsumer
@EnableCompensate
public class SampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

}
