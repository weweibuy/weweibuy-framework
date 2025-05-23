package com.weweibuy.framework.common.mvc.filter;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.support.CopyContentCachingRequestWrapper;
import com.weweibuy.framework.common.core.support.ReadableBodyRequestHandler;
import com.weweibuy.framework.common.core.support.ReadableBodyResponseHandler;
import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

        if (HttpRequestUtils.isIncludePayload(request) && HttpRequestUtils.notBoundaryBody(request.getContentType())) {
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
        request.setAttribute(CommonConstant.HttpServletConstant.REQUEST_TIMESTAMP, System.currentTimeMillis());
    }

}
