package com.weweibuy.framework.samples.client;

import com.weweibuy.framework.common.core.model.dto.CommonDataResponse;
import com.weweibuy.framework.common.feign.support.SnakeCaseEncoderAndDecoder;
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
    CommonDataResponse helloPost(CommonDataResponse<String> commonJsonResponse,
                                 @RequestHeader(value = "token") String token);


    public static class MyRequest extends Request.Options {

        public MyRequest() {
            super(5000, 10000);
        }
    }

}
