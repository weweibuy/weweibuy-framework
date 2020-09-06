package com.weweibuy.framework.common.db.config;

import com.weweibuy.framework.common.db.properties.DBEncryptProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author durenhao
 * @date 2020/9/6 0:05
 **/
@Configuration
@EnableConfigurationProperties({DBEncryptProperties.class})
public class CommonDBConfig {
}
