package com.weweibuy.framework.common.mvc.config;

import com.weweibuy.framework.common.log.config.CommonLogProperties;
import com.weweibuy.framework.common.log.support.HttpLogConfigurer;
import com.weweibuy.framework.common.mvc.constant.HealthCheckConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import jakarta.servlet.ServletContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author durenhao
 * @date 2020/9/25 21:16
 **/
public class HealthLogDisableConfigurer implements HttpLogConfigurer {

    @Autowired
    private Optional<ServletContext> servletContext;

    @Override
    public void addHttpLogConfig(List<CommonLogProperties.HttpPathProperties> logHttpPropertiesMap) {
        String contextPath = servletContext
                .map(ServletContext::getContextPath)
                .orElse("");

        CommonLogProperties.HttpPathProperties commonLogHttpProperties = new CommonLogProperties.HttpPathProperties();
        commonLogHttpProperties.setPath(contextPath + HealthCheckConstant.HEALTH_CHECK_PATH);
        commonLogHttpProperties.setMethods(Collections.singleton(HttpMethod.GET));

        CommonLogProperties.LogProperties logProperties = new CommonLogProperties.LogProperties();
        logProperties.setDisableReq(true);
        logProperties.setDisableResp(true);

        commonLogHttpProperties.setLog(logProperties);
        logHttpPropertiesMap.add(commonLogHttpProperties);
    }
}
