package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.common.mvc.resolver.annotation.SnakeCaseRequestParamBody;
import com.weweibuy.framework.samples.model.dto.CommonDataJsonResponse;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author durenhao
 * @date 2020/2/17 22:14
 **/
@RestController
@RequestMapping("/mvc")
public class MvcController {

    @GetMapping("/test")
    public Object request(@Valid MvcUser user) {
        return user;
    }

    @GetMapping("/test-snake")
    public Object request2(@SnakeCaseRequestParamBody @Valid MvcUser user) {
        return CommonDataJsonResponse.success(user);
    }

    @GetMapping("/test-param")
    public Object request3(@RequestParam String userName) {
        return userName;
    }

    @PostMapping("/test-json")
    public Object request4(@RequestBody @Valid  MvcUser user) {
        return CommonDataJsonResponse.success(user);
    }

        @Data
    public static class MvcUser {

        @NotBlank(message = "userName 不能为空")
        private String userName;

        @NotNull
        private Integer userAge;


        public MvcUser() {
        }

        public MvcUser(String userName, Integer userAge) {
            this.userName = userName;
            this.userAge = userAge;
        }
    }

}
