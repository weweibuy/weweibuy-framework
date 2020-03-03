package com.weweibuy.framework.samples.client;

import com.weiweibuy.framework.common.feign.support.SnakeCaseEncoderAndDecoder;
import com.weweibuy.framework.samples.model.dto.CommonDataJsonResponse;
import feign.Request;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author durenhao
 * @date 2020/3/2 19:54
 **/
@FeignClient(name = "myFeignClient2", url = "http://localhost:9000", configuration = {SnakeCaseEncoderAndDecoder.class, MyFeignClient2.MyRequest.class})
public interface MyFeignClient2 {

    @PostMapping("/hello")
    CommonDataJsonResponse<String> helloPost(CommonDataJsonResponse<String> commonJsonResponse,
                                             @RequestHeader(value = "token") String token);


    public static class MyRequest extends Request.Options {

        public MyRequest() {
            super(5000, 10000);
        }
    }

}
