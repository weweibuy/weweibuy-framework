package com.weweibuy.framework.common.log.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * @author durenhao
 * @date 2020/6/24 23:34
 **/
@Data
@ConfigurationProperties(prefix = "common.log")
public class CommonLogProperties {

    private Boolean enable = true;

    private CommonLogHttpProperties http = new CommonLogHttpProperties();

    @Data
    public static class CommonLogHttpProperties {

        /**
         * 配置形式:  RES_/**; RESP_/**; /**
         */
        private Set<String> disablePath = new HashSet<>();

    }


}
