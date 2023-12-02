package com.weweibuy.framework.common.db.config;

import com.weweibuy.framework.common.db.aspect.SpecDataSourceAspect;
import com.weweibuy.framework.common.db.aspect.SpecDataSourceBeanFactoryPointcutAdvisor;
import com.weweibuy.framework.common.db.aspect.SpecDataSourcePointcut;
import com.weweibuy.framework.common.db.encrypt.AESEncryptHelper;
import com.weweibuy.framework.common.db.properties.DBEncryptProperties;
import com.weweibuy.framework.common.db.properties.MultipleDatasourceAndMybatisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;

/**
 * @author durenhao
 * @date 2020/9/6 0:05
 **/
@AutoConfiguration
@EnableConfigurationProperties({DBEncryptProperties.class})
public class CommonDBConfig {

    @Autowired
    private MultipleDatasourceAndMybatisProperties properties;


    @Bean
    @ConditionalOnProperty(name = "common.db.encrypt.enable", havingValue = "true")
    public AESEncryptHelper aesEncryptHelper() {
        return new AESEncryptHelper();
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
