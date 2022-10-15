package com.weweibuy.framework.compensate.mybatis.config;

import com.weweibuy.framework.compensate.config.CompensateAutoConfiguration;
import com.weweibuy.framework.compensate.core.CompensateStore;
import com.weweibuy.framework.compensate.mybatis.mapper.CompensateMapper;
import com.weweibuy.framework.compensate.mybatis.repository.CompensateRepository;
import com.weweibuy.framework.compensate.mybatis.store.JdbcCompensateStore;
import com.weweibuy.framework.compensate.mybatis.support.JdbcCompensateRecorder;
import com.weweibuy.framework.compensate.support.CompensateRecorder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * JDBC 补偿配置
 *
 * @author durenhao
 * @date 2020/2/16 22:34
 **/
@AutoConfiguration
@ConditionalOnBean(CompensateAutoConfiguration.class)
// TODO 优化这里
@MapperScan(basePackageClasses = CompensateMapper.class)
public class JdbcCompensateStoreConfig {

    @Bean
    public CompensateStore jdbcCompensateStore() {
        return new JdbcCompensateStore();
    }

    @Bean
    @Primary
    public CompensateRecorder jdbcCompensateRecorder() {
        return new JdbcCompensateRecorder();
    }


    @Bean
    public CompensateRepository compensateRepository() {
        return new CompensateRepository();
    }

}
