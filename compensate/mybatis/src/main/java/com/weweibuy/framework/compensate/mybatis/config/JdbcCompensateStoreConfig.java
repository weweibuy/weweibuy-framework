package com.weweibuy.framework.compensate.mybatis.config;

import com.weweibuy.framework.compensate.config.CompensateConfigurationSupport;
import com.weweibuy.framework.compensate.interfaces.CompensateStore;
import com.weweibuy.framework.compensate.mybatis.mapper.CompensateMapper;
import com.weweibuy.framework.compensate.mybatis.store.JdbcCompensateStore;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author durenhao
 * @date 2020/2/16 22:34
 **/
@Configuration
@ConditionalOnBean(CompensateConfigurationSupport.class)
@MapperScan(basePackageClasses = CompensateMapper.class)
public class JdbcCompensateStoreConfig {

    @Bean
    public CompensateStore jdbcCompensateStore() {
        return new JdbcCompensateStore();
    }

}
