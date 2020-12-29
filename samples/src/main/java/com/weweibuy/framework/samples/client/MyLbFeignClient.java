package com.weweibuy.framework.samples.client;

import com.weweibuy.framework.common.core.model.dto.CommonDataResponse;
import com.weweibuy.framework.common.feign.support.SnakeCaseEncoderAndDecoder;
import feign.Request;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author durenhao
 * @date 2020/3/2 19:54
 **/
@FeignClient(name = "learning-feign-provider" , contextId = "MyLbFeignClient", configuration = {SnakeCaseEncoderAndDecoder.class, MyLbFeignClient.MyRequest.class})
public interface MyLbFeignClient {

    @PostMapping("/hello")
    CommonDataResponse<String> helloPost(CommonDataResponse<String> commonJsonResponse,
                                         @RequestHeader(value = "token") String token);


    @GetMapping("/hello")
    CommonDataResponse<String> helloGet(@RequestParam(value = "msg") String msg,
                                        @RequestHeader(value = "token") String token);


    public static class MyRequest extends Request.Options {

        public MyRequest() {
            super(5000, 10000);
        }
    }

}
