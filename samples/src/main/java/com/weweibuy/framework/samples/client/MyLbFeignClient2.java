package com.weweibuy.framework.samples.client;

import com.weweibuy.framework.common.core.model.dto.CommonDataResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author durenhao
 * @date 2020/3/2 19:54
 **/
@FeignClient(name = "learning-feign-consume" , contextId = "MyLbFeignClient2")
public interface MyLbFeignClient2 {

    @GetMapping("/hello")
    CommonDataResponse<String> helloGet();


}
