package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.common.core.model.dto.CommonDataResponse;
import com.weweibuy.framework.common.feign.support.MultipartFileHelper;
import com.weweibuy.framework.samples.client.*;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author durenhao
 * @date 2020/3/2 20:01
 **/
@RestController
@RequiredArgsConstructor
public class FeignController {

    private final MyFeignClient myFeignClient;

    private final MyFeignClient2 myFeignClient2;

    private final FileClient fileClient;

    private final MyLbFeignClient myLbFeignClient;

    private final MyLbFeignClient2 myLbFeignClient2;


    @GetMapping("/feign")
    public Object sendToFeign() {
        CommonDataResponse<String> token_123 = myFeignClient.helloPost(CommonDataResponse.success(""), "token_123");
        myLbFeignClient2.helloGet();
        return myLbFeignClient.helloPost(CommonDataResponse.success(null), "token_123");
    }

    @PostMapping("/upload")
    public ResponseEntity<StreamingResponseBody> uploadFile(MultipartFile file, String name) throws IOException {
        MultipartFile multipartFile = MultipartFileHelper.createMultipartFile(file.getInputStream(), file.getOriginalFilename());
        Response response = fileClient.uploadFile(multipartFile, name);
        InputStream inputStream = response.body().asInputStream();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name)
                .body(outputStream ->
                {
                    byte[] buf = new byte[1024];
                    int len = 0;
                    while ((len = inputStream.read(buf)) != -1) {
                        outputStream.write(buf);
                    }
                });
    }

}
