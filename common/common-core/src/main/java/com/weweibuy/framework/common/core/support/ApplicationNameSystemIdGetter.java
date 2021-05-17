package com.weweibuy.framework.common.core.support;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;

/**
 * @author durenhao
 * @date 2021/5/17 22:34
 **/
@RequiredArgsConstructor
public class ApplicationNameSystemIdGetter implements SystemIdGetter {

    private final Environment environment;


    @Override
    public String getSystemId() {
        return environment.getProperty("spring.application.name");
    }
}
