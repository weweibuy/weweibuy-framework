package com.weweibuy.framework.common.db.config;

import com.weweibuy.framework.common.db.multiple.MultipleDatasourceRegister;
import com.weweibuy.framework.common.db.properties.MultipleDataSourceProperties;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 多数据源配置
 *
 * @author durenhao
 * @date 2021/7/18 9:48
 **/
@Configuration
@EnableConfigurationProperties({MultipleDataSourceProperties.class})
@ConditionalOnProperty(prefix = "common.db", name = "multipleDatasource")
@RequiredArgsConstructor
@ConditionalOnMissingBean({DataSourceAutoConfiguration.class, MybatisAutoConfiguration.class})
public class MultipleDatasourceConfig {

    @Bean
    public MultipleDatasourceRegister multipleDatasourceRegister() {
        return new MultipleDatasourceRegister();
    }


}
