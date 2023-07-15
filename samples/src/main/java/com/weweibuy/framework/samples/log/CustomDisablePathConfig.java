package com.weweibuy.framework.samples.log;

import com.weweibuy.framework.common.log.config.CommonLogProperties;
import com.weweibuy.framework.common.log.support.HttpLogConfigurer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/7/25 23:36
 **/
@Component
public class CustomDisablePathConfig implements HttpLogConfigurer {

    @Override
    public void addHttpLogConfig(List<CommonLogProperties.HttpPathProperties> logHttpProperties) {
    }
}
