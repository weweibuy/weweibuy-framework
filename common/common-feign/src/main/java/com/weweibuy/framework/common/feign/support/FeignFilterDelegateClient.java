package com.weweibuy.framework.common.feign.support;

import feign.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * 过滤请求响应  FeignClient
 *
 * @author durenhao
 * @date 2021/10/30 17:21
 **/
@Order(0)
@RequiredArgsConstructor
public class FeignFilterDelegateClient implements DelegateFeignClient {

    @Autowired(required = false)
    private List<FeignFilter> feignFilterList;

    @Override
    public Client delegate(Client client) {
        return new FilterRequestFeignClient(client, feignFilterList);
    }
}
