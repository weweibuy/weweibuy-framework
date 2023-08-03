package com.weweibuy.framework.common.log.mvc;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.support.LogTraceCodeGetter;
import com.weweibuy.framework.common.log.support.LogTraceContext;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 设置MDC的 traceCode过滤器
 *
 * @author durenhao
 * @date 2020/3/14 15:46
 **/
@Order(Integer.MIN_VALUE + 1000)
public class TraceCodeFilter extends OncePerRequestFilter {

    private final LogTraceCodeGetter<HttpServletRequest> logTraceCodeGetter = new HttpSimpleLoggerCodeGetter();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String traceCode = logTraceCodeGetter.getTraceCode(request);
        LogTraceContext.setTraceCodeAndUserCode(traceCode,
                logTraceCodeGetter.getUserCode(request));
        try {
            response.addHeader(CommonConstant.LogTraceConstant.HTTP_TRACE_CODE_HEADER, traceCode);
            filterChain.doFilter(request, response);
        } finally {
            LogTraceContext.clear();
        }
    }
}
