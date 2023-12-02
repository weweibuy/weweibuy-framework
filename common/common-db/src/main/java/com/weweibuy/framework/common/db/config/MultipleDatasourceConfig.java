package com.weweibuy.framework.common.db.config;

import com.weweibuy.framework.common.db.multiple.MultipleDatasourceAndMybatisRegister;
import com.weweibuy.framework.common.db.properties.MultipleDatasourceAndMybatisProperties;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 多数据源配置
 * 排除 springBoot 自动配置:
 * -- @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, MybatisAutoConfiguration.class, XADataSourceAutoConfiguration.class})
 *
 * @author durenhao
 * @date 2021/7/18 9:48
 **/
@AutoConfiguration
@EnableConfigurationProperties({MultipleDatasourceAndMybatisProperties.class})
public class MultipleDatasourceConfig {


    @Bean
    @ConditionalOnMissingBean({DataSourceAutoConfiguration.class, MybatisAutoConfiguration.class})
    public MultipleDatasourceAndMybatisRegister multipleDatasourceRegister() {
        return new MultipleDatasourceAndMybatisRegister();
    }


}
