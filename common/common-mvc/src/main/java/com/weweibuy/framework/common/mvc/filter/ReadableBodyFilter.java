package com.weweibuy.framework.common.mvc.filter;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.support.CopyContentCachingRequestWrapper;
import com.weweibuy.framework.common.core.support.ReadableBodyRequestHandler;
import com.weweibuy.framework.common.core.support.ReadableBodyResponseHandler;
import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author durenhao
 * @date 2023/2/19 10:46
 **/
@Slf4j
@Order(-103)
public class ReadableBodyFilter extends OncePerRequestFilter {

    private List<ReadableBodyRequestHandler> readableBodyRequestHandler;

    private List<ReadableBodyResponseHandler> readableBodyResponseHandler;

    public ReadableBodyFilter(List<ReadableBodyRequestHandler> readableBodyRequestHandler,
                              List<ReadableBodyResponseHandler> readableBodyResponseHandler) {
        this.readableBodyRequestHandler = Optional.ofNullable(readableBodyRequestHandler).orElse(Collections.emptyList());
        this.readableBodyResponseHandler = Optional.ofNullable(readableBodyResponseHandler).orElse(Collections.emptyList());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        setRequestAttributes(request);

        boolean includePayload = HttpRequestUtils.isIncludePayload(request);
        if (includePayload) {
            request = new CopyContentCachingRequestWrapper(request);
        }
        response = new ContentCachingResponseWrapper(response);

        try {
            boolean shouldReturn = handlerReadableBodyRequest(request, response);
            if (shouldReturn) {
                return;
            }
            filterChain.doFilter(request, response);

            handlerReadableBodyResponse(request, (ContentCachingResponseWrapper) response);
        } finally {
            ((ContentCachingResponseWrapper) response).copyBodyToResponse();
        }
    }

    private boolean handlerReadableBodyRequest(HttpServletRequest request, HttpServletResponse response) {

        boolean exit = false;
        for (ReadableBodyRequestHandler handler : readableBodyRequestHandler) {
            boolean continueNext = handler.handlerReadableBodyRequest(request, response);
            if (!continueNext) {
                exit = true;
                break;
            }
        }
        return exit;
    }


    private void handlerReadableBodyResponse(HttpServletRequest request, ContentCachingResponseWrapper response) {
        for (ReadableBodyResponseHandler handler : readableBodyResponseHandler) {
            boolean continueNext = handler.handlerReadableBodyResponse(request, response);
            if (!continueNext) {
                break;
            }
        }
    }


    private void setRequestAttributes(HttpServletRequest request) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        requestAttributes.setAttribute(CommonConstant.HttpServletConstant.REQUEST_METHOD, request.getMethod(), RequestAttributes.SCOPE_REQUEST);
        requestAttributes.setAttribute(CommonConstant.HttpServletConstant.REQUEST_PATH, request.getRequestURI(), RequestAttributes.SCOPE_REQUEST);
        requestAttributes.setAttribute(CommonConstant.HttpServletConstant.REQUEST_CONTENT_TYPE, request.getContentType(), RequestAttributes.SCOPE_REQUEST);
        requestAttributes.setAttribute(CommonConstant.HttpServletConstant.REQUEST_PARAMETER_MAP, request.getParameterMap(), RequestAttributes.SCOPE_REQUEST);
        requestAttributes.setAttribute(CommonConstant.HttpServletConstant.REQUEST_TIMESTAMP, System.currentTimeMillis(), RequestAttributes.SCOPE_REQUEST);
    }

}
