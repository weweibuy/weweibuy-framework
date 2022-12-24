package com.weweibuy.framework.common.db.config;

import com.weweibuy.framework.common.db.multiple.MultipleDatasourceRegister;
import com.weweibuy.framework.common.db.properties.MultipleDataSourceProperties;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 多数据源配置
 * 排除 springBoot 自动配置:
 * -- @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, MybatisAutoConfiguration.class, XADataSourceAutoConfiguration.class})
 *
 * @author durenhao
 * @date 2021/7/18 9:48
 **/
@AutoConfiguration
@ConditionalOnProperty(prefix = "common.db.multiple-datasource", name = "enable", havingValue = "true")
@EnableConfigurationProperties({MultipleDataSourceProperties.class})
public class MultipleDatasourceConfig {

    @Bean
    @ConditionalOnMissingBean({DataSourceAutoConfiguration.class, MybatisAutoConfiguration.class})
    public MultipleDatasourceRegister multipleDatasourceRegister() {
        return new MultipleDatasourceRegister();
    }


}
