package com.weweibuy.framework.common.feign.support;

import com.weweibuy.framework.common.feign.log.FeignLogger;
import feign.Logger;
import feign.Request;
import feign.Response;
import org.springframework.core.annotation.Order;

import java.io.IOException;

/**
 * feign 日志输出
 * <p>
 * 该日志输出比feign自身的日志输出{@link Logger}更为底层
 * 适用于增加 FeignFilter 过滤器时,对响应进行处理, 有针对性的抛出异常时, 用改过滤器可以输出日志,方式漏输出日志
 * eg: 对响应进行验签
 *
 * @author durenhao
 * @date 2021/10/30 17:40
 **/
@Order(Integer.MAX_VALUE - 100)
public class LogFeignFilter implements FeignFilter {

    @Override
    public Response filter(Request request, Request.Options options, FeignFilterChain chain) throws IOException {
        logRequest(request);
        long millis = System.currentTimeMillis();
        Response response = null;
        try {
            response = chain.doFilter(request, options);
        } catch (Exception e) {
            logAndReBufferResponse(request, response, millis);
            throw e;
        }
        return logAndReBufferResponse(request, response, millis);

    }

    private void logRequest(Request request) {
        FeignLogger.logFilterRequest(request);
    }

    private Response logAndReBufferResponse(Request request, Response response, long startTimeMillis) throws IOException {
        return FeignLogger.logAndReBufferFilterResponse(request, response, startTimeMillis);
    }

}
