package com.weweibuy.framework.samples.client;

import feign.form.spring.SpringFormEncoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传下载
 *
 * @author durenhao
 * @date 2020/7/26 15:15
 **/
@FeignClient(name = "fileClient", url = "http://localhost:9000", configuration = SpringFormEncoder.class)
public interface FileClient {

    @PostMapping(value = "/hello-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity uploadFile(@RequestPart("file") MultipartFile file, @RequestParam("name") String name);

}
