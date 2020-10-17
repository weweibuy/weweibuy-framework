package com.weweibuy.framework.common.log.mvc;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import com.weweibuy.framework.common.log.desensitization.SensitizationMappingConfigurer;
import com.weweibuy.framework.common.log.desensitization.SensitizationMappingOperator;
import com.weweibuy.framework.common.log.logger.HttpLogger;
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
import java.util.Optional;

/**
 * @author durenhao
 * @date 2020/3/1 10:07
 **/
@Slf4j
@Order(-103)
public class RequestLogContextFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String contentType = request.getContentType();

        boolean includePayload = HttpRequestUtils.isIncludePayload(request);
        if (includePayload) {
            request = new ContentCachingRequestWrapper(request);
        }
        setRequestAttributes(request);
        // 非json请求
        if (StringUtils.isBlank(contentType) || !MediaType.valueOf(contentType).isCompatibleWith(MediaType.APPLICATION_JSON) || !includePayload) {
            HttpLogger.logForNotJsonRequest(request);
        }

        Optional<SensitizationMappingConfigurer.HttpSensitizationMapping> matchRequest = SensitizationMappingOperator.matchRequest(request);

        SensitizationMappingConfigurer.HttpSensitizationMapping mapping = matchRequest.orElse(null);

        if (mapping == null) {
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
