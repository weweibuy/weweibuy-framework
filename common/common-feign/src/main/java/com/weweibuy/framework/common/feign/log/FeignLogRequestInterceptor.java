package com.weweibuy.framework.common.feign.log;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * @author durenhao
 * @date 2020/3/2 20:49
 **/
@Slf4j
public class FeignLogRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        FeignLogger.logForRequest(template.url(), template.headers(), template.requestBody().asString());
    }


}
