package com.weweibuy.framework.common.feign.support;

import com.weweibuy.framework.common.feign.log.HttpClientLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * @author durenhao
 * @date 2022/10/13 21:23
 **/
@Slf4j
public class CustomHttpClientLogInterceptor implements HttpResponseInterceptor, HttpRequestInterceptor {

    private static final String REQ_TIME_KEY = "req_time_key";


    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        context.setAttribute(REQ_TIME_KEY, System.currentTimeMillis());
        try {
            HttpClientLogger.logReq(request);
        } catch (Exception e) {
            log.error("Httpclient 输出请求日志异常: ", e);
        }
    }

    @Override
    public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
        Long time = (Long) context.getAttribute(REQ_TIME_KEY);
        try {
            HttpClientLogger.logResp(response, time);
        } catch (Exception e) {
            log.error("Httpclient 输出响应日志异常: ", e);
        }
    }


}
