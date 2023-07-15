package com.weweibuy.framework.common.feign.mock;

import com.weweibuy.framework.common.feign.support.FeignFilter;
import com.weweibuy.framework.common.feign.support.FeignFilterChain;
import com.weweibuy.framework.common.feign.support.LogFeignFilter;
import feign.Request;
import feign.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import java.io.IOException;

/**
 * @author durenhao
 * @date 2022/10/15 22:33
 **/
@Order(Integer.MAX_VALUE - 100)
public class MockFeignLogFilter implements FeignFilter {

    @Autowired
    private MockFeignDelegateClient mockClient;


    @Override
    public Response filter(Request request, Request.Options options, FeignFilterChain chain) throws IOException {
        MockClient mockClientInstance = mockClient.getMockClientInstance();
        MockClient.MockConfig mockConfig = mockClientInstance.matchMockConfig(request);
        if (mockConfig == null) {
            return chain.doFilter(request, options);
        }
        return LogFeignFilter.filterReq(request, options, chain);
    }


}
