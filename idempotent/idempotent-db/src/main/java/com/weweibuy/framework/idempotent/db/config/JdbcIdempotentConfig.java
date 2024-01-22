package com.weweibuy.framework.idempotent.db.config;

import com.weweibuy.framework.idempotent.core.config.IdempotentConfig;
import com.weweibuy.framework.idempotent.core.support.IdempotentManager;
import com.weweibuy.framework.idempotent.db.JdbcIdempotentManager;
import com.weweibuy.framework.idempotent.db.JdbcIdempotentProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * JDBC 幂等配置
 *
 * @author durenhao
 * @date 2020/6/20 13:17
 **/
@AutoConfiguration
@EnableConfigurationProperties(value = JdbcIdempotentProperties.class)
@ConditionalOnBean({IdempotentConfig.class, JdbcTemplateAutoConfiguration.class})
@ConditionalOnProperty(prefix = "idempotent.jdbc", name = "enable", havingValue = "true", matchIfMissing = true)
public class JdbcIdempotentConfig {

    @Autowired
    private JdbcIdempotentProperties jdbcIdempotentProperties;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Bean
    @Primary
    public IdempotentManager jdbcIdempotentManager() {
        return new JdbcIdempotentManager(jdbcTemplate, jdbcIdempotentProperties);
    }

}
