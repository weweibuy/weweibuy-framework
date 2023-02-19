package com.weweibuy.framework.common.mvc.config;

import com.weweibuy.framework.common.log.config.CommonLogProperties;
import com.weweibuy.framework.common.log.support.HttpLogConfigurer;
import com.weweibuy.framework.common.mvc.constant.HealthCheckConstant;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;

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
    public void addHttpLogConfig(List<CommonLogProperties.CommonLogHttpProperties> logHttpPropertiesMap) {
        String contextPath = servletContext
                .map(ServletContext::getContextPath)
                .orElse("");

        CommonLogProperties.CommonLogHttpProperties commonLogHttpProperties = new CommonLogProperties.CommonLogHttpProperties();
        commonLogHttpProperties.setPath(contextPath + HealthCheckConstant.HEALTH_CHECK_PATH);
        commonLogHttpProperties.setMethod(Collections.singletonList("GET"));
        commonLogHttpProperties.setDisableReq(true);
        commonLogHttpProperties.setDisableResp(true);

        logHttpPropertiesMap.add(commonLogHttpProperties);
    }
}
