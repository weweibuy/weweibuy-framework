package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.mvc.extend.resolver.annotation.SnakeCaseRequestParamBody;
import com.weweibuy.framework.samples.model.dto.CommonDataJsonResponse;
import lombok.Data;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

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

    @RequestMapping("/test-snake")
    public Object request2(@SnakeCaseRequestParamBody @Valid MvcUser user, BindingResult result) {
//        if (true) {
//            throw new RuntimeException("xxx");
//        }
        return CommonDataJsonResponse.success(user);
    }

    @GetMapping("/test-param")
    public Object request3(@RequestParam String userName) {
        return userName;
    }


    @Data
    public static class MvcUser {

        @NotBlank
        private String userName;

        private Integer userAge;


        public MvcUser() {
        }

        public MvcUser(String userName, Integer userAge) {
            this.userName = userName;
            this.userAge = userAge;
        }
    }

}
