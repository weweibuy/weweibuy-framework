package com.weweibuy.framework.common.mvc.config;

import com.weweibuy.framework.common.log.config.LogDisablePath;
import com.weweibuy.framework.common.log.support.LogDisableConfigurer;
import com.weweibuy.framework.common.mvc.constant.HealthCheckConstant;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.Optional;

/**
 * @author durenhao
 * @date 2020/9/25 21:16
 **/
public class HealthLogDisableConfigurer implements LogDisableConfigurer {

    @Autowired
    private Optional<ServletContext> servletContext;

    @Override
    public void addHttpDisableConfig(List<LogDisablePath> disablePathList) {
        String contextPath = servletContext
                .map(ServletContext::getContextPath)
                .orElse("");
        disablePathList.add(LogDisablePath.builder()
                .path(contextPath + HealthCheckConstant.HEALTH_CHECK_PATH).type(LogDisablePath.Type.ALL).build());
    }
}
