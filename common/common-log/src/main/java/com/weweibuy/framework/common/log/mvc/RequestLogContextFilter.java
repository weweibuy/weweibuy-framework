package com.weweibuy.framework.common.log.mvc;

import com.weweibuy.framework.common.core.exception.BusinessException;
import com.weweibuy.framework.common.core.exception.SystemException;
import com.weweibuy.framework.common.core.model.ResponseCodeAndMsg;
import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.model.eum.CommonErrorCodeEum;
import com.weweibuy.framework.common.core.support.ReadableBodyRequestHandler;
import com.weweibuy.framework.common.core.support.ReadableBodyResponseHandler;
import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import com.weweibuy.framework.common.core.utils.JackJsonUtils;
import com.weweibuy.framework.common.log.desensitization.SensitizationMappingConfigurer;
import com.weweibuy.framework.common.log.desensitization.SensitizationMappingOperator;
import com.weweibuy.framework.common.log.logger.HttpLogger;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * @author durenhao
 * @date 2020/3/1 10:07
 **/
@Slf4j
@Order(-103)
public class RequestLogContextFilter extends OncePerRequestFilter {

    @Autowired(required = false)
    private ReadableBodyRequestHandler readableBodyRequestHandler;

    @Autowired(required = false)
    private ReadableBodyResponseHandler readableBodyResponseHandler;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String contentType = request.getContentType();
        response = new ContentCachingResponseWrapper(response);
        boolean includePayload = HttpRequestUtils.isIncludePayload(request);
        if (includePayload) {
            request = new ContentCachingRequestWrapper(request);
        }
        setRequestAttributes(request);
        // 非json请求
        if (StringUtils.isBlank(contentType) || !MediaType.valueOf(contentType).isCompatibleWith(MediaType.APPLICATION_JSON) || !includePayload) {
            HttpLogger.logForNotJsonRequest(request);
            if (readableBodyRequestHandler != null) {
                // 处理可读取的请求体
                boolean shouldReturn = handlerReadableBodyReq(request, response);
                if (shouldReturn) {
                    return;
                }
            }
        }

        Optional<SensitizationMappingConfigurer.HttpSensitizationMapping> matchRequest = SensitizationMappingOperator.matchRequest(request);

        SensitizationMappingConfigurer.HttpSensitizationMapping mapping = matchRequest.orElse(null);

        if (mapping == null) {
            // 不需要脱敏
            filterChain.doFilter(request, response);
        } else {
            // 脱敏上下文绑定
            SensitizationMappingOperator.bindSensitizationContext(mapping.getSensitizationField(), mapping.getLogger());
            try {
                filterChain.doFilter(request, response);
            } finally {
                SensitizationMappingOperator.removeSensitizationContext();
            }
        }
        copyAndLogResponse(request, response);
    }


    private boolean handlerReadableBodyReq(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            readableBodyRequestHandler.handlerReadableBodyRequest(request, response, true);
        } catch (BusinessException e) {
            log.warn("处理请求异常: ", e);
            writeResponse(response, HttpStatus.BAD_REQUEST, e.getCodeAndMsg());
            copyAndLogResponse(request, response);
            return true;
        } catch (SystemException e) {
            log.error("处理请求异常: ", e);
            writeResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, e.getCodeAndMsg());
            copyAndLogResponse(request, response);
            return true;
        } catch (Exception e) {
            log.error("处理请求异常: ", e);
            writeResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, CommonErrorCodeEum.UNKNOWN_EXCEPTION);
            copyAndLogResponse(request, response);
            return true;
        }
        return false;
    }


    private void copyAndLogResponse(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (readableBodyResponseHandler != null) {
            readableBodyResponseHandler.handlerReadableBodyResponse(request, response);
        }
        ContentCachingResponseWrapper cachingResponseWrapper = (ContentCachingResponseWrapper) response;
        InputStream contentInputStream = cachingResponseWrapper.getContentInputStream();
        String body = "";
        String contentType = Optional.ofNullable(response.getContentType())
                .orElse(StringUtils.EMPTY);
        if (!HttpRequestUtils.contentTypeCanLogBody(contentType)) {
            body = HttpRequestUtils.BOUNDARY_BODY;
        } else {
            body = IOUtils.toString(contentInputStream, CommonConstant.CharsetConstant.UT8);
        }
        HttpLogger.logResponseBody(body, response.getStatus());
        cachingResponseWrapper.copyBodyToResponse();
    }


    private HttpServletResponse cacheResponse(HttpServletResponse response) {
        if (readableBodyResponseHandler != null) {
            return new ContentCachingResponseWrapper(response);
        }
        return response;
    }


    private void setRequestAttributes(HttpServletRequest request) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        requestAttributes.setAttribute(CommonConstant.HttpServletConstant.REQUEST_METHOD, request.getMethod(), RequestAttributes.SCOPE_REQUEST);
        requestAttributes.setAttribute(CommonConstant.HttpServletConstant.REQUEST_PATH, request.getRequestURI(), RequestAttributes.SCOPE_REQUEST);
        requestAttributes.setAttribute(CommonConstant.HttpServletConstant.REQUEST_CONTENT_TYPE, request.getContentType(), RequestAttributes.SCOPE_REQUEST);
        requestAttributes.setAttribute(CommonConstant.HttpServletConstant.REQUEST_PARAMETER_MAP, request.getParameterMap(), RequestAttributes.SCOPE_REQUEST);
        requestAttributes.setAttribute(CommonConstant.HttpServletConstant.REQUEST_TIMESTAMP, System.currentTimeMillis(), RequestAttributes.SCOPE_REQUEST);
    }

    private void writeResponse(HttpServletResponse response, HttpStatus httpStatus, ResponseCodeAndMsg responseCodeAndMsg) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(CommonConstant.CharsetConstant.UTF8_STR);
        response.setStatus(httpStatus.value());
        response.getWriter().write(JackJsonUtils.writeWithMvc(responseCodeAndMsg));
    }


}
