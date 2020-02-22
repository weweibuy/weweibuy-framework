package com.weweibuy.framework.rocketmq.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ResourceLoader;

/**
 * @author durenhao
 * @date 2020/2/22 18:31
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourcesUtils {

    /**
     * 解析  ${}  配置为实际配置
     *
     * @param value
     * @param resourceLoader
     * @return
     */
    public static String resolve(String value, ResourceLoader resourceLoader) {
        if (StringUtils.isNoneBlank(value)
                && resourceLoader instanceof ConfigurableApplicationContext) {
            return ((ConfigurableApplicationContext) resourceLoader).getEnvironment()
                    .resolvePlaceholders(value);
        }
        return value;
    }

}
