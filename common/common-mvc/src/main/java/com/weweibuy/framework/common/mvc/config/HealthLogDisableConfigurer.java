package com.weweibuy.framework.common.mvc.config;

import com.weweibuy.framework.common.log.config.LogDisablePath;
import com.weweibuy.framework.common.log.support.LogDisableConfigurer;
import com.weweibuy.framework.common.mvc.constant.HealthCheckConstant;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/9/25 21:16
 **/
public class HealthLogDisableConfigurer implements LogDisableConfigurer {

    @Override
    public void addHttpDisableConfig(List<LogDisablePath> disablePathList) {
        disablePathList.add(LogDisablePath.builder()
                .path(HealthCheckConstant.HEALTH_CHECK_PATH).type(LogDisablePath.Type.ALL).build());
    }
}
