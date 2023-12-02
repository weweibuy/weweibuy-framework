package com.weweibuy.framework.common.db.config;

import com.weweibuy.framework.common.db.aspect.SpecDatasourceAspect;
import com.weweibuy.framework.common.db.aspect.SpecDatasourceBeanFactoryPointcutAdvisor;
import com.weweibuy.framework.common.db.aspect.SpecDatasourcePointcut;
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
    @ConditionalOnProperty(name = "common.db.multiple.enable-spec-datasource", havingValue = "true", matchIfMissing = true)
    public SpecDatasourceBeanFactoryPointcutAdvisor compensateBeanFactoryPointcutAdvisor() {
        Integer order = properties.getSpecDatasourceAspectOrder();
        SpecDatasourceBeanFactoryPointcutAdvisor advisor = new SpecDatasourceBeanFactoryPointcutAdvisor();
        advisor.setPc(new SpecDatasourcePointcut());
        advisor.setOrder(order);
        advisor.setAdvice(new SpecDatasourceAspect());
        return advisor;
    }

}
