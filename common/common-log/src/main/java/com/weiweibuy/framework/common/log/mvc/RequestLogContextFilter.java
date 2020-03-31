package com.weiweibuy.framework.common.log.mvc;

import com.weiweibuy.framework.common.core.utils.JackJsonUtils;
import com.weiweibuy.framework.common.log.context.RequestLogContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author durenhao
 * @date 2020/3/1 10:07
 **/
@Slf4j
@Order(500)
public class RequestLogContextFilter extends OncePerRequestFilter {

    private Map<String, Set<String>> stringSetMap;

    public RequestLogContextFilter(Map<String, Set<String>> stringSetMap) {
        this.stringSetMap = stringSetMap;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String contentType = request.getContentType();
        try {
            RequestLogContext.put(request, stringSetMap);
            if (StringUtils.isBlank(contentType) || !MediaType.valueOf(contentType).isCompatibleWith(MediaType.APPLICATION_JSON)) {
                log.info("请求路径: {}, Method: {},  数据: {}", request.getRequestURI(), request.getMethod(),
                        JackJsonUtils.write(parameterMapToString(request.getParameterMap())));
            }
            filterChain.doFilter(request, response);
        } finally {
            RequestLogContext.clear();
            MDC.clear();
        }
    }

    private Map<String, String> parameterMapToString(Map<String, String[]> parameterMap) {
        Map<String, String> stringStringHashMap = new HashMap<>(parameterMap.size());
        parameterMap.forEach((k, v) -> {
            stringStringHashMap.put(k, Arrays.stream(v)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining(",", "", "")));
        });
        return stringStringHashMap;
    }
}
