package com.weweibuy.framework.common.db.config;

import com.weweibuy.framework.common.db.aspect.SpecDataSourceAspect;
import com.weweibuy.framework.common.db.aspect.SpecDataSourceBeanFactoryPointcutAdvisor;
import com.weweibuy.framework.common.db.aspect.SpecDataSourcePointcut;
import com.weweibuy.framework.common.db.multiple.MultipleDatasourceAndMybatisRegister;
import com.weweibuy.framework.common.db.properties.MultipleDatasourceAndMybatisProperties;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
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
@RequiredArgsConstructor
public class MultipleDatasourceConfig {

    private final MultipleDatasourceAndMybatisProperties properties;

    @Bean
    @ConditionalOnMissingBean({DataSourceAutoConfiguration.class, MybatisAutoConfiguration.class})
    public MultipleDatasourceAndMybatisRegister multipleDatasourceRegister() {
        return new MultipleDatasourceAndMybatisRegister();
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @ConditionalOnProperty(name = "common.db.enable-spec-datasource", havingValue = "true", matchIfMissing = true)
    public SpecDataSourceBeanFactoryPointcutAdvisor compensateBeanFactoryPointcutAdvisor() {
        Integer order = properties.getSpecDatasourceAspectOrder();
        SpecDataSourceBeanFactoryPointcutAdvisor advisor = new SpecDataSourceBeanFactoryPointcutAdvisor();
        advisor.setPc(new SpecDataSourcePointcut());
        advisor.setOrder(order);
        advisor.setAdvice(new SpecDataSourceAspect());
        return advisor;
    }

}
