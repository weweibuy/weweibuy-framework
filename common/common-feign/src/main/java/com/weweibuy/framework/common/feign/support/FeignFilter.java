package com.weweibuy.framework.common.feign.support;

import feign.Request;
import feign.Response;

/**
 * feign 请求过滤器
 * <p>
 * 执行顺序
 * Encoder -->  RequestInterceptor  -->  FeignFilter -->  Decoder --> FeignClient
 * <p>
 * 通过 @Order 标记执行顺序
 *
 * @author durenhao
 * @date 2021/10/30 16:56
 **/
public interface FeignFilter {


    /**
     * 过滤
     *
     * @param request
     * @param options
     * @param chain
     * @return
     */
    Response filter(Request request, Request.Options options, FeignFilterChain chain);


}
