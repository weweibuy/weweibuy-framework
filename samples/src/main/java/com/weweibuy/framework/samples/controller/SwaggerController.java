package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.common.core.model.dto.CommonCodeResponse;
import com.weweibuy.framework.common.core.model.dto.CommonDataResponse;
import com.weweibuy.framework.common.core.utils.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * @author durenhao
 * @date 2023/10/30 14:34
 **/
@Api(tags = "sawgger接口")
@RestController
@RequestMapping("/swagger")
public class SwaggerController {

    @GetMapping("/demo")
    @ApiOperation(value = "swagger-demo接口")
    public CommonCodeResponse query() {
        return CommonCodeResponse.success();
    }


}
