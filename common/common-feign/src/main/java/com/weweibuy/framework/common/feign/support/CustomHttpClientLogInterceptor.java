package com.weweibuy.framework.common.feign.support;

import com.weweibuy.framework.common.feign.config.HttpClientProperties;
import com.weweibuy.framework.common.feign.log.HttpClientLogger;
import com.weweibuy.framework.common.log.config.CommonLogProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author durenhao
 * @date 2022/10/13 21:23
 **/
@Slf4j
public class CustomHttpClientLogInterceptor implements HttpResponseInterceptor, HttpRequestInterceptor {

    private static final String REQ_TIME_KEY = "req_time_key";

    private final HttpClientProperties httpClientProperties;

    /**
     * 精确匹配
     */
    private Map<String, HttpClientProperties.LogHttpProperties> methodPathExactProperties = new HashMap<>();

    /**
     * 路径匹配
     */
    private Map<String, List<HttpClientProperties.LogHttpProperties>> methodPatternProperties = new HashMap<>();


    public CustomHttpClientLogInterceptor(HttpClientProperties httpClientProperties) {
        this.httpClientProperties = httpClientProperties;
    }

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
