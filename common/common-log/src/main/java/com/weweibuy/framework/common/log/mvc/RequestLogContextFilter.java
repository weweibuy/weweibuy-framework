package com.weweibuy.framework.common.log.mvc;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.log.context.RequestLogContext;
import com.weweibuy.framework.common.log.logger.HttpLogger;
import com.weweibuy.framework.common.log.utils.HttpRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @author durenhao
 * @date 2020/3/1 10:07
 **/
@Slf4j
@Order(500)
public class RequestLogContextFilter extends OncePerRequestFilter {

    /**
     * 需要 进行脱敏的 请求路径地址与字段
     */
    private Map<String, Set<String>> stringSetMap;

    public RequestLogContextFilter(Map<String, Set<String>> stringSetMap) {
        this.stringSetMap = stringSetMap;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String contentType = request.getContentType();
        try {
            boolean includePayload = HttpRequestUtils.isIncludePayload(request);
            if (includePayload) {
                request = new ContentCachingRequestWrapper(request);
            }
            // TODO 对需要脱敏的进行绑定
            RequestLogContext.put(request, stringSetMap);
            setRequestAttributes(request);
            // 非json请求
            if (StringUtils.isBlank(contentType) || !MediaType.valueOf(contentType).isCompatibleWith(MediaType.APPLICATION_JSON) || !includePayload) {
                HttpLogger.logForNotJsonRequest(request);
            }
            filterChain.doFilter(request, response);
        } finally {
            RequestLogContext.clear();
        }
    }

    private void setRequestAttributes(HttpServletRequest request) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        requestAttributes.setAttribute(CommonConstant.HttpServletConstant.REQUEST_METHOD, request.getMethod(), RequestAttributes.SCOPE_REQUEST);
        requestAttributes.setAttribute(CommonConstant.HttpServletConstant.REQUEST_CONTENT_TYPE, request.getContentType(), RequestAttributes.SCOPE_REQUEST);
        requestAttributes.setAttribute(CommonConstant.HttpServletConstant.REQUEST_PARAMETER_MAP, request.getParameterMap(), RequestAttributes.SCOPE_REQUEST);
        requestAttributes.setAttribute(CommonConstant.HttpServletConstant.REQUEST_TIMESTAMP, System.currentTimeMillis(), RequestAttributes.SCOPE_REQUEST);
    }


}
