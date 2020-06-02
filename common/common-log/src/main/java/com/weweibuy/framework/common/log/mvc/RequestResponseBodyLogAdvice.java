package com.weweibuy.framework.common.log.mvc;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.utils.JackJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        String bodyStr = readRequestBody(requestAttributes, body);
        String path = getAttribute(requestAttributes, "org.springframework.web.servlet.HandlerMapping.lookupPath");
        String httpMethod = getAttribute(requestAttributes, CommonConstant.HttpServletConstant.REQUEST_METHOD);
        log.info("请求路径: {}, Method: {}, 数据: {}", path, httpMethod, bodyStr);
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
        Long timestamp = getAttribute(RequestContextHolder.getRequestAttributes(), CommonConstant.HttpServletConstant.REQUEST_TIMESTAMP);
        log.info("响应数据: {}, 请求耗时: {}", JackJsonUtils.write(body), System.currentTimeMillis() - timestamp);
        return body;
    }

    @SuppressWarnings("unchecked")
    private <T> T getAttribute(RequestAttributes requestAttributes, String name) {
        return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
    }


    private String readRequestBody(RequestAttributes requestAttributes, Object body) {
        if (requestAttributes instanceof ServletRequestAttributes) {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            HttpServletRequest request = servletRequestAttributes.getRequest();
            if (request instanceof ContentCachingRequestWrapper) {
                ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
                String charEncoding = requestWrapper.getCharacterEncoding() != null ? requestWrapper.getCharacterEncoding() : CommonConstant.CharsetConstant.UTF8_STR;
                try {
                    return new String(requestWrapper.getContentAsByteArray(), charEncoding);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return JackJsonUtils.write(body);
    }

}
