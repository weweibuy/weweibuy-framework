package com.weweibuy.framework.samples.compensate.config;

import com.weweibuy.framework.compensate.core.CompensateStore;
import com.weweibuy.framework.compensate.support.BizIdCompensateAssemble;
import com.weweibuy.framework.compensate.support.BizIdCompensateTypeResolver;
import com.weweibuy.framework.compensate.support.CompensateTypeResolver;
import com.weweibuy.framework.samples.compensate.JdbcCompensateStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author durenhao
 * @date 2020/2/16 12:21
 **/
@Configuration
public class CompensateConfig {

    @Bean
    public CompensateStore jdbcCompensateStore() {
        return new JdbcCompensateStore();
    }

    @Bean
    public CompensateTypeResolver bizIdCompensateTypeResolver(BizIdCompensateAssemble assemble) {
        return new BizIdCompensateTypeResolver(assemble);
    }
}
