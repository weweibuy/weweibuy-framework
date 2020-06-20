package com.weweibuy.framework.idempotent.db.config;

import com.weweibuy.framework.idempotent.core.support.IdempotentManager;
import com.weweibuy.framework.idempotent.db.JdbcIdempotentManager;
import com.weweibuy.framework.idempotent.db.JdbcIdempotentProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author durenhao
 * @date 2020/6/20 13:17
 **/
@Configuration
@EnableConfigurationProperties(value = JdbcIdempotentProperties.class)
//@ConditionalOnBean(JdbcTemplate.class)
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
