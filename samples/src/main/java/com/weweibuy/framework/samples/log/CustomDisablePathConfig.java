package com.weweibuy.framework.samples.log;

import com.weweibuy.framework.common.log.config.LogDisablePath;
import com.weweibuy.framework.common.log.support.LogDisableConfigurer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/7/25 23:36
 **/
@Component
public class CustomDisablePathConfig implements LogDisableConfigurer {

    @Override
    public void addHttpDisableConfig(List<LogDisablePath> disablePathList) {
        disablePathList.add(LogDisablePath.builder().path("/hello").type(LogDisablePath.Type.REQ).build());
        disablePathList.add(LogDisablePath.builder().path("/hello").type(LogDisablePath.Type.REQ).build());

    }
}
