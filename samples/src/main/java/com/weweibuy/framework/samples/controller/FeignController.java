package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.samples.client.FileClient;
import com.weweibuy.framework.samples.client.MyFeignClient;
import com.weweibuy.framework.samples.client.MyFeignClient2;
import com.weweibuy.framework.samples.model.dto.CommonDataJsonResponse;
import feign.Response;
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
public class FeignController {

    private final MyFeignClient myFeignClient;

    private final MyFeignClient2 myFeignClient2;

    private final FileClient fileClient;

    public FeignController(MyFeignClient myFeignClient, MyFeignClient2 myFeignClient2, FileClient fileClient) {
        this.myFeignClient = myFeignClient;
        this.myFeignClient2 = myFeignClient2;
        this.fileClient = fileClient;
    }

    @GetMapping("/feign")
    public Object sendToFeign() {
        return myFeignClient.helloPost(CommonDataJsonResponse.success(null), "token_123");
    }

    @PostMapping("/upload")
    public ResponseEntity<StreamingResponseBody> uploadFile(MultipartFile file, String name) throws IOException {
        Response response = fileClient.uploadFile(file, name);
        try (InputStream inputStream = response.body().asInputStream()) {
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

}
