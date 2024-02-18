package com.weweibuy.framework.samples.controller.feign;

import com.weweibuy.framework.common.core.model.dto.CommonCodeResponse;
import com.weweibuy.framework.common.core.model.dto.CommonDataResponse;
import com.weweibuy.framework.common.core.utils.JackJsonUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author durenhao
 * @date 2022/10/14 22:34
 **/
@RestController
@RequestMapping("feign-test")
public class FeignTestController {


    @GetMapping("/get")
    public String getReq() throws Exception {
        return IOUtils.toString(new FileInputStream("C:\\Users\\G7er\\Desktop\\豆瓣电影 Top 250.html"));
    }

    @GetMapping("/get-text")
    public void getTextReq(HttpServletResponse response) throws IOException {
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE);
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        String str = JackJsonUtils.writeCamelCase(CommonCodeResponse.success());
        writer.write(str);
    }


    @PostMapping("/post-from")
    public CommonCodeResponse getFrom(CommonDataResponse commonDataResponse) {
        return CommonCodeResponse.success();
    }

    @PostMapping("/upload-down")
    public ResponseEntity<StreamingResponseBody> uploadDown(MultipartFile file, String name, HttpServletResponse response) throws IOException {
        InputStream inputStream = file.getInputStream();
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


    @GetMapping("/get-json")
    public CommonCodeResponse getJson(@RequestBody CommonDataResponse commonDataResponse) {
        return CommonCodeResponse.success();
    }

    @PostMapping("/post-json")
    public CommonCodeResponse postJson(@RequestBody CommonDataResponse commonDataResponse) {
        return CommonCodeResponse.success();
    }

    @GetMapping("/get204")
    public ResponseEntity get204() {
        return ResponseEntity.status(204).build();
    }

}
