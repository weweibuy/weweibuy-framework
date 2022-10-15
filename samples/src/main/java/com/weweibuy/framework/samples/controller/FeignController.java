package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.model.dto.CommonCodeResponse;
import com.weweibuy.framework.common.core.model.dto.CommonDataResponse;
import com.weweibuy.framework.common.feign.support.MultipartFileHelper;
import com.weweibuy.framework.samples.client.FileClient;
import com.weweibuy.framework.samples.client.LocalFeignClient;
import com.weweibuy.framework.samples.client.LocalFileFeignClient;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
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

    private final CloseableHttpClient httpClient;

    private static final ContentType TEXT_HTML_UTF8_CONTENT_TYPE = ContentType.create(MediaType.TEXT_HTML_VALUE, CommonConstant.CharsetConstant.UT8);


    @GetMapping("/feign")
    public Object sendToFeign() throws IOException {
        CommonCodeResponse json = localFeignClient.getReq();

        localFeignClient.get204();
        Response textReq = localFeignClient.getTextReq(CommonDataResponse.success("querymap"));

        CommonCodeResponse json1 = localFeignClient.getJson(CommonDataResponse.success("getJson"));
        CommonCodeResponse postJson = localFeignClient.postJson(CommonDataResponse.success("postJson"));
        CommonCodeResponse getFrom = localFileFeignClient.getFrom(CommonDataResponse.success("getFrom"));

        File file = new File("C:\\Users\\z\\Desktop\\Flowable原生Api问题.txt");
        MultipartFile multipartFile = MultipartFileHelper.createMultipartFile(file);
        Response response = localFileFeignClient.uploadFile(multipartFile, file.getName());

//        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
//                .setCharset(CommonConstant.CharsetConstant.UT8);
//        builder.addTextBody("key", "value", TEXT_HTML_UTF8_CONTENT_TYPE);
//        builder.addBinaryBody("file", file);
//        HttpEntity entity = builder.build();
//        HttpPost httpPost = new HttpPost("https://www.baidu.com");
//        httpPost.setEntity(entity);
//
//        try (CloseableHttpResponse execute = httpClient.execute(httpPost)) {
//        }
//        HttpGet httpGet = new HttpGet("https://imgcps.jd.com/ling4/100032149194/5Lqs6YCJ5aW96LSn/5L2g5YC85b6X5oul5pyJ/p-5f3a47329785549f6bc7a6ec/996c3f09/cr/s/q.jpg");
//        try (CloseableHttpResponse execute = httpClient.execute(httpGet)) {
//
//        }
//        httpPost = new HttpPost("https://www.baidu.com");
//        try (CloseableHttpResponse execute = httpClient.execute(httpPost)) {
//
//        }
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
