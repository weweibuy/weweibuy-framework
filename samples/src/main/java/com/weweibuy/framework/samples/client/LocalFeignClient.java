package com.weweibuy.framework.samples.client;

import com.weweibuy.framework.common.core.model.dto.CommonCodeResponse;
import com.weweibuy.framework.common.core.model.dto.CommonDataResponse;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author durenhao
 * @date 2022/10/14 22:51
 **/
@FeignClient(name = "localFeignClient", url = "http://localhost:8080/feign-test")
public interface LocalFeignClient {


    @GetMapping("/get")
    CommonCodeResponse getReq();

    @GetMapping("/get-text")
    Response getTextReq(@SpringQueryMap CommonDataResponse commonDataResponse);

    @GetMapping("/get-json")
    CommonCodeResponse getJson(@RequestBody CommonDataResponse commonDataResponse);

    @PostMapping("/post-json")
    CommonCodeResponse postJson(@RequestBody CommonDataResponse commonDataResponse);

    @GetMapping("/get204")
    void get204();
}
