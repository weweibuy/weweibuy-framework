package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.samples.client.MyFeignClient;
import com.weweibuy.framework.samples.model.dto.CommonDataJsonResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author durenhao
 * @date 2020/3/2 20:01
 **/
@RestController
public class FeignController {

    private final MyFeignClient myFeignClient;

    public FeignController(MyFeignClient myFeignClient) {
        this.myFeignClient = myFeignClient;
    }

    @GetMapping("/feign")
    public Object sendToFeign() {
        return myFeignClient.helloPost(CommonDataJsonResponse.success(null), "token_123");
    }


}
