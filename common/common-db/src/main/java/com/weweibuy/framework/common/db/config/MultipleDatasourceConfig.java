package com.weweibuy.framework.common.db.config;

import com.weweibuy.framework.common.db.aspect.SpecDatasourceAspect;
import com.weweibuy.framework.common.db.aspect.SpecDatasourceBeanFactoryPointcutAdvisor;
import com.weweibuy.framework.common.db.aspect.SpecDatasourcePointcut;
import com.weweibuy.framework.common.db.multiple.MultipleDatasourceAndMybatisRegister;
import com.weweibuy.framework.common.db.properties.MultipleDatasourceAndMybatisProperties;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;

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
@ConditionalOnMissingBean({DataSourceAutoConfiguration.class, MybatisAutoConfiguration.class})
public class MultipleDatasourceConfig {


    @Bean
    public MultipleDatasourceAndMybatisRegister multipleDatasourceRegister() {
        return new MultipleDatasourceAndMybatisRegister();
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @ConditionalOnProperty(name = "common.db.multiple.enable-spec-datasource", havingValue = "true", matchIfMissing = true)
    public SpecDatasourceBeanFactoryPointcutAdvisor compensateBeanFactoryPointcutAdvisor(MultipleDatasourceAndMybatisProperties properties) {
        Integer order = properties.getSpecDatasourceAspectOrder();
        SpecDatasourceBeanFactoryPointcutAdvisor advisor = new SpecDatasourceBeanFactoryPointcutAdvisor();
        advisor.setPc(new SpecDatasourcePointcut());
        advisor.setOrder(order);
        advisor.setAdvice(new SpecDatasourceAspect());
        return advisor;
    }


}
