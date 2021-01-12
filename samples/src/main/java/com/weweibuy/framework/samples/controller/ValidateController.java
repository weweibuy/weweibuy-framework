package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.common.core.validate.annotation.Maximum;
import com.weweibuy.framework.common.core.validate.annotation.Minimum;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author durenhao
 * @date 2021/1/12 22:29
 **/
@RestController
@RequestMapping("/validate")
public class ValidateController {

    @PostMapping
    public Object validate(@RequestBody @Valid Model model) {
        return model;
    }


    @Data
    public static class Model {

        @Minimum(value = 0, message = "数量必须大于0")
        private Long minQty;

        @Maximum(value = 10, message = "数量必须小于10")
        private Long maxQty;


    }


}
