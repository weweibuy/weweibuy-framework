package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.common.core.model.dto.CommonCodeResponse;
import com.weweibuy.framework.common.core.utils.JackJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author durenhao
 * @date 2023/6/11 15:58
 **/
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {


    @PostMapping("/data")
    public CommonCodeResponse data(@RequestBody Map<String, Object> data) {
        log.info("数据: {}", JackJsonUtils.writeCamelCase(data));
        return CommonCodeResponse.success();
    }

}
