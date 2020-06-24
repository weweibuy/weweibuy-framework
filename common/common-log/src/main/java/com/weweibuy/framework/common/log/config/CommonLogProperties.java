package com.weweibuy.framework.common.log.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

/**
 * @author durenhao
 * @date 2020/6/24 23:34
 **/
@Data
@ConfigurationProperties(prefix = "common.log")
public class CommonLogProperties {

    private Boolean enable;

    private CommonLogHttpProperties http;

    @Data
    public static class CommonLogHttpProperties {

        private Set<String> disablePath;

    }
}
