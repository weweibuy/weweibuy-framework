package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.common.core.model.dto.CommonDataResponse;
import com.weweibuy.framework.common.mvc.desensitization.SensitiveData;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @author durenhao
 * @date 2020/3/4 21:42
 **/
@RestController
@RequestMapping("/sensitive")
public class SensitiveController {


    @GetMapping
    public Object sensitive(MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        OutputStream outputStream = new FileOutputStream("C:\\Users\\z\\Desktop\\tmp/test.txt");
        IOUtils.copy(inputStream, outputStream);
        outputStream.close();
        SensitiveModel build = SensitiveModel.builder()
                .userName("张三")
                .idCard("61210115475475125654751X")
                .address("上海市虹桥区机场路1号")
                .bankCard("622220185251513232233225")
                .phone("13800000000")
                .age(12).build();
        return CommonDataResponse.success(build);
    }

    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\z\\Desktop\\ES.txt");
        String s = DigestUtils.md5DigestAsHex(fileInputStream);
        System.err.println(s);

    }

    @Data
    @Builder
    public static class SensitiveModel {

        @SensitiveData(patten = "(?<=[\\u4e00-\\u9fa5]{1})(.*)", replace = "*")
        private String userName;

        private String idCard;

        private String address;

        private String bankCard;

        private String phone;

        private Integer age;


    }

}
