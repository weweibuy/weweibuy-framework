package com.weweibuy.framework.samples.client;

import com.weweibuy.framework.common.feign.support.SnakeCaseEncoderAndDecoder;
import com.weweibuy.framework.samples.model.dto.CommonDataJsonResponse;
import feign.Request;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author durenhao
 * @date 2020/3/2 19:54
 **/
@FeignClient(name = "webuy-auth" , contextId = "MyLbFeignClient", configuration = {SnakeCaseEncoderAndDecoder.class, MyLbFeignClient.MyRequest.class})
public interface MyLbFeignClient {

    @PostMapping("/gw/authorize")
    CommonDataJsonResponse helloPost(CommonDataJsonResponse<String> commonJsonResponse,
                                             @RequestHeader(value = "token") String token);


    public static class MyRequest extends Request.Options {

        public MyRequest() {
            super(5000, 10000);
        }
    }

}
