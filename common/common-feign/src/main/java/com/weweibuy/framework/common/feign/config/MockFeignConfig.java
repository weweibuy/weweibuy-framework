package com.weweibuy.framework.common.feign.config;

import com.weweibuy.framework.common.feign.mock.MockClient;
import feign.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * @author durenhao
 * @date 2021/3/13 23:10
 **/
@Profile(value = {"mock"})
@Configuration
public class MockFeignConfig {

    @Bean
    @Primary
    public Client mockClient(Client client) {
        return new MockClient(client);
    }


}
