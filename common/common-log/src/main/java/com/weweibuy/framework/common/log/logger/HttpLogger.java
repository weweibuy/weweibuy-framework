package com.weweibuy.framework.common.log.logger;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import com.weweibuy.framework.common.core.utils.JackJsonUtils;
import com.weweibuy.framework.common.log.config.LogDisablePath;
import com.weweibuy.framework.common.log.constant.LogMdcConstant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * http 请求响应日志输出
 * TODO 区分请求响应?
 *
 * @author durenhao
 * @date 2020/6/3 22:34
 **/
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpLogger {

    private static Map<String, LogDisablePath.Type> patternDisabledPath = Collections.emptyMap();

    private static Map<String, LogDisablePath.Type> exactDisabledPath = Collections.emptyMap();

    public static void logForJsonRequest(String path, String method, Map<String, String[]> parameterMap, String body) {
        if (!shouldLogRequest(path)) {
            return;
        }
        if (parameterMap != null && !parameterMap.isEmpty()) {
            log.info("Http 请求路径: {}, Method: {}, 参数: {} , Body: {}",
                    path,
                    method,
                    HttpRequestUtils.parameterMapToString(parameterMap),
                    body);
        } else {
            log.info("Http 请求路径: {}, Method: {}, Body: {}",
                    path,
                    method,
                    body);
        }
        RequestContextHolder.getRequestAttributes().setAttribute(
                LogMdcConstant.HAS_LOG_REQ_FIELD_NAME, true, RequestAttributes.SCOPE_REQUEST);
    }

    public static void logForNotJsonRequest(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (!shouldLogRequest(path)) {
            return;
        }
        log.info("Http 请求路径: {}, Method: {}, 参数: {}",
                path,
                request.getMethod(),
                HttpRequestUtils.parameterMapToString(request.getParameterMap()));
    }


    public static void logResponseBody(String body, Integer status) {
        String path = HttpRequestUtils.getRequestAttribute(RequestContextHolder.getRequestAttributes(), CommonConstant.HttpServletConstant.REQUEST_PATH);
        if (!shouldLogResponse(path)) {
            return;
        }
        Long timestamp = HttpRequestUtils.getRequestAttribute(RequestContextHolder.getRequestAttributes(), CommonConstant.HttpServletConstant.REQUEST_TIMESTAMP);
        log.info("Http 响应 Status: {}, 数据: {}, 请求耗时: {}",
                status,
                body,
                System.currentTimeMillis() - timestamp);
    }


    public static void logForJsonBodyRequest(Object body) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        logForJsonRequest(requestAttributes, body);
    }


    public static void determineAndLogForJsonRequest(HttpServletRequest request) {
        if (HttpRequestUtils.isJsonRequest(request.getContentType())) {
            logForJsonRequest(request, true);
        }
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
                .map(JackJsonUtils::writeWithMvc)
                .orElse(StringUtils.EMPTY);
    }

    public static void configDisabledPath(Set<LogDisablePath> patternDisabledPath,
                                          Set<LogDisablePath> exactDisabledPath) {
        HttpLogger.patternDisabledPath = patternDisabledPath.stream()
                .peek(d -> d.setPath(HttpRequestUtils.sanitizedPath(d.getPath())))
                .collect(Collectors.toMap(LogDisablePath::getPath, LogDisablePath::getType, (o, n) -> n));
        HttpLogger.exactDisabledPath = exactDisabledPath.stream()
                .peek(d -> d.setPath(HttpRequestUtils.sanitizedPath(d.getPath())))
                .collect(Collectors.toMap(LogDisablePath::getPath, LogDisablePath::getType, (o, n) -> n));
    }

    private static boolean shouldLogRequest(String path) {
        if (StringUtils.isBlank(path)) {
            return false;
        }
        Boolean hasLogged = Optional.ofNullable(HttpRequestUtils.<Boolean>getRequestAttribute(RequestContextHolder.getRequestAttributes(),
                        LogMdcConstant.HAS_LOG_REQ_FIELD_NAME))
                .orElse(false);

        if (hasLogged != null && hasLogged) {
            return false;
        }
        LogDisablePath.Type type = null;
        if ((type = exactDisabledPath.get(path)) != null &&
                (LogDisablePath.Type.REQ.equals(type) || LogDisablePath.Type.ALL.equals(type))) {
            return false;
        }
        return !patternDisabledPath.entrySet().stream()
                .anyMatch(p -> HttpRequestUtils.isMatchPath(p.getKey(), path) &&
                        (LogDisablePath.Type.REQ.equals(p.getValue()) || LogDisablePath.Type.ALL.equals(p.getValue())));
    }


    private static boolean shouldLogResponse(String path) {
        if (StringUtils.isBlank(path)) {
            return false;
        }
        LogDisablePath.Type type = null;
        if ((type = exactDisabledPath.get(path)) != null &&
                (LogDisablePath.Type.RESP.equals(type) || LogDisablePath.Type.ALL.equals(type))) {
            return false;
        }
        return !patternDisabledPath.entrySet().stream()
                .anyMatch(p -> HttpRequestUtils.isMatchPath(p.getKey(), path) &&
                        (LogDisablePath.Type.RESP.equals(p.getValue()) || LogDisablePath.Type.ALL.equals(p.getValue())));
    }


}
