package com.weweibuy.framework.common.log.mvc;

import com.weweibuy.framework.common.core.utils.IdWorker;
import com.weweibuy.framework.common.log.constant.LogMdcConstant;
import org.slf4j.MDC;
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
@Order(0)
public class TraceCodeFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            MDC.put(LogMdcConstant.TID_FIELD_NAME, IdWorker.nextStringId());
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(LogMdcConstant.TID_FIELD_NAME);
        }
    }
}
