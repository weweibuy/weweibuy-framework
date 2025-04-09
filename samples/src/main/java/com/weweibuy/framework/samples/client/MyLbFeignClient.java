package com.weweibuy.framework.samples.client;

import com.weweibuy.framework.common.core.model.dto.CommonDataResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author durenhao
 * @date 2020/3/2 19:54
 **/
@FeignClient(name = "test-application" , contextId = "MyLbFeignClient")
public interface MyLbFeignClient {

    @PostMapping("/test/test")
    CommonDataResponse<String> helloPost();




}
