package com.weweibuy.framework.common.feign.mock;

import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.framework.common.feign.support.DelegateFeignClient;
import feign.Client;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;

import java.io.IOException;

/**
 * mock feign
 *
 * @author durenhao
 * @date 2021/11/1 0:03
 **/
@Order(Integer.MAX_VALUE)
@DependsOn("jackJsonUtils")
public class MockFeignDelegateClient implements DelegateFeignClient {

    private MockClient mockClientInstance;

    @Override
    public Client delegate(Client client) {
        MockClient mockClient = new MockClient(client);
        try {
            mockClient.init();
        } catch (IOException e) {
            throw Exceptions.uncheckedIO(e);
        }
        mockClientInstance = mockClient;
        return mockClient;
    }

    public MockClient getMockClientInstance() {
        return mockClientInstance;
    }
}
