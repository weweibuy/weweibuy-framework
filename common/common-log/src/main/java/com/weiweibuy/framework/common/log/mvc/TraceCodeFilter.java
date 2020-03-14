package com.weiweibuy.framework.common.log.mvc;

import com.weiweibuy.framework.common.log.constant.LogMdcConstant;
import com.weweibuy.webuy.common.utils.IdWorker;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
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
