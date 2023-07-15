package com.weweibuy.framework.samples.client;

import com.weweibuy.framework.common.core.model.dto.CommonCodeResponse;
import com.weweibuy.framework.common.core.model.dto.CommonDataResponse;
import feign.Response;
import feign.form.spring.SpringFormEncoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author durenhao
 * @date 2022/10/14 22:51
 **/
@FeignClient(name = "localFileFeignClient", url = "http://localhost:8080/feign-test", configuration = SpringFormEncoder.class)
public interface LocalFileFeignClient {


    @PostMapping(value = "/upload-down", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Response uploadFile(@RequestPart("file") MultipartFile file, @RequestParam("name") String name);

    @PostMapping(value = "/post-from", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    CommonCodeResponse getFrom(CommonDataResponse commonDataResponse);


}
