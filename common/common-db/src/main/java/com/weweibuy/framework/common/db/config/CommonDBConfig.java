package com.weweibuy.framework.common.db.config;

import com.weweibuy.framework.common.db.encrypt.AESEncryptHelper;
import com.weweibuy.framework.common.db.properties.DBEncryptProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author durenhao
 * @date 2020/9/6 0:05
 **/
@AutoConfiguration
@EnableConfigurationProperties({DBEncryptProperties.class})
public class CommonDBConfig {


    @Bean
    @ConditionalOnProperty(name = "common.db.encrypt.enable", havingValue = "true")
    public AESEncryptHelper aesEncryptHelper() {
        return new AESEncryptHelper();
    }

}
