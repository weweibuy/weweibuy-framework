package com.weweibuy.framework.common.log.logger;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.utils.JackJsonUtils;
import com.weweibuy.framework.common.log.utils.HttpRequestUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

/**
 * http 请求响应日志输出
 *
 * @author durenhao
 * @date 2020/6/3 22:34
 **/
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpLogger {

    public static void logForJsonRequest(String path, String method, Map<String, String[]> parameterMap, String body) {
        if (parameterMap != null && !parameterMap.isEmpty()) {
            log.info("请求路径: {}, Method: {}, 参数: {} , Body:{}",
                    path,
                    method,
                    HttpRequestUtils.parameterMapToString(parameterMap),
                    body);
        } else {
            log.info("请求路径: {}, Method: {}, Body:  {}",
                    path,
                    method,
                    body);
        }
    }

    public static void logForNotJsonRequest(HttpServletRequest request) {
        log.info("请求路径: {}, Method: {}, 参数: {}",
                request.getRequestURI(),
                request.getMethod(),
                HttpRequestUtils.parameterMapToString(request.getParameterMap()));
    }

    public static void logResponse(Object body) {
        Long timestamp = HttpRequestUtils.getRequestAttribute(RequestContextHolder.getRequestAttributes(), CommonConstant.HttpServletConstant.REQUEST_TIMESTAMP);
        log.info("响应数据: {}, 请求耗时: {}",
                JackJsonUtils.write(body),
                System.currentTimeMillis() - timestamp);
    }

    public static void logForJsonBodyRequest(Object body) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        logForJsonRequest(requestAttributes, body);
    }

    public static void logForJsonRequest(HttpServletRequest request, boolean useWrapper) {
        logForJsonRequest(request.getRequestURI(), request.getMethod(), request.getParameterMap(),
                HttpRequestUtils.readRequestBodyForJson(request, useWrapper));
    }

    public static void logForJsonRequest(RequestAttributes requestAttributes, Object body) {
        String bodyStr = readRequestBody(requestAttributes, body);
        String path = HttpRequestUtils.getRequestAttribute(requestAttributes, CommonConstant.HttpServletConstant.REQUEST_PATH);
        String httpMethod = HttpRequestUtils.getRequestAttribute(requestAttributes, CommonConstant.HttpServletConstant.REQUEST_METHOD);
        Map<String, String[]> parameterMap = HttpRequestUtils.getRequestAttribute(requestAttributes, CommonConstant.HttpServletConstant.REQUEST_PARAMETER_MAP);
        logForJsonRequest(path, httpMethod, parameterMap, bodyStr);
    }

    static String readRequestBody(RequestAttributes requestAttributes, Object body) {
        if (requestAttributes instanceof ServletRequestAttributes) {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            HttpServletRequest request = servletRequestAttributes.getRequest();
            if (request instanceof ContentCachingRequestWrapper) {
                return HttpRequestUtils.readFromRequestWrapper((ContentCachingRequestWrapper) request);
            }
        }
        return Optional.ofNullable(body)
                .map(JackJsonUtils::write)
                .orElse(StringUtils.EMPTY);
    }

}
