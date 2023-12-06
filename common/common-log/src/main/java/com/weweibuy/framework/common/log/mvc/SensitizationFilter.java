package com.weweibuy.framework.common.log.mvc;

import com.weweibuy.framework.common.log.config.CommonLogProperties;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 脱敏上下文绑定过滤器
 *
 * @author durenhao
 * @date 2023/2/18 23:19
 **/
@Order(-104)
public class SensitizationFilter extends OncePerRequestFilter {

    private MvcPathMappingOperator mvcPathMappingOperator;

    public SensitizationFilter(MvcPathMappingOperator mvcPathMappingOperator) {
        this.mvcPathMappingOperator = mvcPathMappingOperator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        CommonLogProperties.SensitizationProperties sensitizationProperties = mvcPathMappingOperator.findSensitizationProperties(request);

        if (sensitizationProperties == null) {
            // 不需要脱敏
            filterChain.doFilter(request, response);
            return;
        }

        // 脱敏上下文绑定
        MvcPathMappingOperator.bindSensitizationContext(sensitizationProperties.getSensitizationFields(),
                sensitizationProperties.getLogger());
        try {
            filterChain.doFilter(request, response);
        } finally {
            MvcPathMappingOperator.removeSensitizationContext();
        }
    }


}
