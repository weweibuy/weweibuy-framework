package com.weweibuy.framework.common.feign.support;

import feign.Request;
import feign.Response;

import java.io.IOException;

/**
 * feign 请求过滤器链
 *
 * @author durenhao
 * @date 2021/10/30 17:10
 **/
public interface FeignFilterChain {

    /**
     * 请求过滤器链
     *
     * @param request
     * @param options (Message or Collection<Message>)
     * @return
     * @throws Throwable
     */
    Response doFilter(Request request, Request.Options options) throws IOException;


}
