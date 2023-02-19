package com.weweibuy.framework.common.log.mvc;

import com.weweibuy.framework.common.log.config.CommonLogProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 脱敏上下文绑定过滤器
 *
 * @author durenhao
 * @date 2023/2/18 23:19
 **/
public class SensitizationFilter extends OncePerRequestFilter {

    private MvcPathMappingOperator mvcPathMappingOperator;

    public SensitizationFilter(MvcPathMappingOperator mvcPathMappingOperator) {
        this.mvcPathMappingOperator = mvcPathMappingOperator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        CommonLogProperties.CommonLogHttpProperties logProperties = mvcPathMappingOperator.findLogProperties(request);

        if (logProperties == null || logProperties.getSensitization() == null)  {
            // 不需要脱敏
            filterChain.doFilter(request, response);
            return;
        }
        CommonLogProperties.HttpSensitizationProperties sensitization = logProperties.getSensitization();

        // 脱敏上下文绑定
        MvcPathMappingOperator.bindSensitizationContext(sensitization.getSensitizationFields(),
                sensitization.getLogger());
        try {
            filterChain.doFilter(request, response);
        } finally {
            MvcPathMappingOperator.removeSensitizationContext();
        }
    }


}
