package com.weweibuy.framework.common.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

/**
 * spring 资源文件及配置工具
 *
 * @author durenhao
 * @date 2020/2/22 18:31
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpringResourcesUtils {

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

    /**
     * 绑定配置文件
     *
     * @param name 配置name (前缀)
     * @param clazz
     * @param environment
     * @param <T>
     * @return
     */
    public static <T> T bindConfig(String name, Class<T> clazz, Environment environment) {
        BindResult<T> restServiceBindResult = Binder.get(environment)
                .bind(name, clazz);
        return restServiceBindResult.get();
    }

}
