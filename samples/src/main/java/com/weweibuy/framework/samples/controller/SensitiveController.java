package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.common.mvc.desensitization.SensitiveData;
import com.weweibuy.framework.samples.model.dto.CommonDataJsonResponse;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author durenhao
 * @date 2020/3/4 21:42
 **/
@RestController
@RequestMapping("/sensitive")
public class SensitiveController {


    @GetMapping
    public Object sensitive() {
        SensitiveModel build = SensitiveModel.builder()
                .userName("张三")
                .idCard("61210115475475125654751X")
                .address("上海市虹桥区机场路1号")
                .bankCard("622220185251513232233225")
                .phone("13800000000")
                .age(12).build();
        return CommonDataJsonResponse.success(build);
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
