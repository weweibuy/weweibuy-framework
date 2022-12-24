package com.weweibuy.framework.common.log.mvc;

import com.weweibuy.framework.common.core.support.ReadableBodyRequestHandler;
import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import com.weweibuy.framework.common.log.logger.HttpLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对于 Json 请求 却没有用 @RequestBody 接受时 在此输出日志
 *
 * @author durenhao
 * @date 2020/6/5 21:12
 **/
@RequiredArgsConstructor
public class UnRequestBodyJsonLogInterceptor implements HandlerInterceptor {

    private final ReadableBodyRequestHandler readableBodyRequestHandler;

    private static final ConcurrentHashMap<HandlerMethod, Boolean> LOG_MAP = new ConcurrentHashMap<>(32);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (HttpRequestUtils.isJsonRequest(request.getContentType()) && handler instanceof HandlerMethod) {
            Boolean match = LOG_MAP.computeIfAbsent((HandlerMethod) handler, key ->
                    Arrays.stream(key.getMethodParameters()).anyMatch(this::supportsRequestBodyParameter));

            if (!match) {
                HttpLogger.logForJsonRequest(request, false);
                if (readableBodyRequestHandler != null) {
                    readableBodyRequestHandler.handlerReadableBodyRequest(request, response, false);
                }
            }
        }
        return true;
    }


    public boolean supportsRequestBodyParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestBody.class);
    }

}
