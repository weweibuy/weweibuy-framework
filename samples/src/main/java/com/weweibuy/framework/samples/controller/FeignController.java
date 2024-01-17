package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.model.dto.CommonCodeResponse;
import com.weweibuy.framework.common.core.model.dto.CommonDataResponse;
import com.weweibuy.framework.common.feign.support.MultipartFileHelper;
import com.weweibuy.framework.samples.client.FileClient;
import com.weweibuy.framework.samples.client.LocalFeignClient;
import com.weweibuy.framework.samples.client.LocalFileFeignClient;
import com.weweibuy.framework.samples.client.MyFeignClient;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author durenhao
 * @date 2020/3/2 20:01
 **/
@RestController
@RequiredArgsConstructor
public class FeignController {

    private final FileClient fileClient;

    private final LocalFeignClient localFeignClient;

    private final LocalFileFeignClient localFileFeignClient;

    private final MyFeignClient myFeignClient;

    private final CloseableHttpClient httpClient;

    private static final ContentType TEXT_HTML_UTF8_CONTENT_TYPE = ContentType.create(MediaType.TEXT_HTML_VALUE, CommonConstant.CharsetConstant.UT8);


    @GetMapping("/feign")
    public Object sendToFeign() throws Exception {

        CommonCodeResponse postJson = localFeignClient.postJson(CommonDataResponse.success("postJson"));

        CommonCodeResponse json = localFeignClient.getReq();

        localFeignClient.get204();
        Response textReq = localFeignClient.getTextReq(CommonDataResponse.success("querymap"));

        CommonCodeResponse json1 = localFeignClient.getJson(CommonDataResponse.success("getJson"));
        CommonCodeResponse getFrom = localFileFeignClient.getFrom(CommonDataResponse.success("getFrom"));

        File file = new File("C:\\Users\\z\\Desktop\\Flowable原生Api问题.txt");
        MultipartFile multipartFile = MultipartFileHelper.createMultipartFile(file);
        Response response = localFileFeignClient.uploadFile(multipartFile, file.getName());

        return CommonCodeResponse.success();
    }

    @PostMapping("/upload")
    public ResponseEntity<StreamingResponseBody> uploadFile(MultipartFile file, String name) throws IOException {
        MultipartFile multipartFile = MultipartFileHelper.createMultipartFile(file.getInputStream(), file.getOriginalFilename());
        Response response = fileClient.uploadFile(multipartFile, name);
        InputStream inputStream = response.body().asInputStream();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getOriginalFilename())
                .body(outputStream ->
                        IOUtils.copy(inputStream, outputStream));
    }

}
