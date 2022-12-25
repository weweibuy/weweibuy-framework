package com.weweibuy.framework.samples;

import com.weweibuy.framework.compensate.annotation.EnableCompensate;
import com.weweibuy.framework.idempotent.core.annotation.EnableIdempotent;
import com.weweibuy.framework.rocketmq.annotation.EnableRocket;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author durenhao
 * @date 2019/12/28 20:02
 **/
@EnableCaching
@SpringBootApplication
@EnableCompensate
@EnableRocket
@EnableFeignClients
@EnableIdempotent
@EnableTransactionManagement(order = 1000)
@EnableDiscoveryClient
public class SampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

}
