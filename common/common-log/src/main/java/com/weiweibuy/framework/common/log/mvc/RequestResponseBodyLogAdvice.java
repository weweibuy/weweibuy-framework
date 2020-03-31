package com.weiweibuy.framework.common.log.mvc;

import com.weiweibuy.framework.common.core.utils.JackJsonUtils;
import com.weiweibuy.framework.common.log.context.RequestLogContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author durenhao
 * @date 2020/2/29 23:48
 **/
@Slf4j
@ControllerAdvice
public class RequestResponseBodyLogAdvice implements RequestBodyAdvice, ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        RequestLogContext requestContext = RequestLogContext.getRequestContext();
        String path = "";
        String httpMethod = "";
        if (requestContext != null) {
            path = requestContext.getPath();
            httpMethod = requestContext.getHttpMethod();
        }
        log.info("请求路径: {}, Method: {}, 数据: {}", path, httpMethod, JackJsonUtils.write(body));
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }


    // ================ ResponseBodyAdvice =================

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        RequestLogContext requestContext = RequestLogContext.getRequestContext();
        if (requestContext != null) {
            Long timestamp = requestContext.getTimestamp();
            log.info("响应数据: {}, 请求耗时: {}", JackJsonUtils.write(body), System.currentTimeMillis() - timestamp);
        } else {
            log.info("响应数据: {}", JackJsonUtils.write(body));
        }
        return body;
    }

}
