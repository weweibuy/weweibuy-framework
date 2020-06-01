package com.weweibuy.framework.samples.client;

import com.weweibuy.framework.common.feign.support.SnakeCaseEncoderAndDecoder;
import com.weweibuy.framework.samples.model.dto.CommonDataJsonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author durenhao
 * @date 2020/3/2 19:54
 **/
@FeignClient(name = "myFeignClient", url = "http://localhost:9000", configuration = SnakeCaseEncoderAndDecoder.class)
public interface MyFeignClient {

    @PostMapping("/hello")
    CommonDataJsonResponse<String> helloPost(CommonDataJsonResponse<String> commonJsonResponse,
                                             @RequestHeader(value = "token") String token);


}
