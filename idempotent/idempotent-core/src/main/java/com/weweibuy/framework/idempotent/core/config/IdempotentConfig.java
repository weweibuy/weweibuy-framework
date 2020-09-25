package com.weweibuy.framework.idempotent.core.config;

import com.weweibuy.framework.idempotent.core.annotation.EnableIdempotent;
import com.weweibuy.framework.idempotent.core.aspect.IdempotentAspect;
import com.weweibuy.framework.idempotent.core.aspect.IdempotentBeanFactoryPointcutAdvisor;
import com.weweibuy.framework.idempotent.core.aspect.IdempotentPointcut;
import com.weweibuy.framework.idempotent.core.support.AnnotationMetaDataHolder;
import com.weweibuy.framework.idempotent.core.support.IdempotentInfoParser;
import com.weweibuy.framework.idempotent.core.support.IdempotentManager;
import com.weweibuy.framework.idempotent.core.support.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @author durenhao
 * @date 2020/3/31 15:01
 **/
@Configuration
public class IdempotentConfig implements ImportAware {

    protected AnnotationAttributes enableCompensate;

    @Autowired
    private Map<String, IdempotentManager> idempotentManagerMap;

    @Autowired(required = false)
    private Map<String, KeyGenerator> keyGeneratorMap;

    @Autowired
    private IdempotentManager idempotentManager;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableCompensate = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableIdempotent.class.getName(), false));
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public IdempotentBeanFactoryPointcutAdvisor idempotentBeanFactoryPointcutAdvisor() {
        IdempotentBeanFactoryPointcutAdvisor advisor = new IdempotentBeanFactoryPointcutAdvisor();
        advisor.setPc(new IdempotentPointcut(idempotentAnnotationMetaDataHolder()));
        advisor.setOrder(enableCompensate.<Integer>getNumber("order"));
        advisor.setAdvice(new IdempotentAspect(idempotentInfoParser()));
        return advisor;
    }

    @Bean
    @DependsOn("jackJsonUtils")
    public AnnotationMetaDataHolder idempotentAnnotationMetaDataHolder() {
        return new AnnotationMetaDataHolder();
    }

    @Bean
    public IdempotentInfoParser idempotentInfoParser() {
        return new IdempotentInfoParser(idempotentAnnotationMetaDataHolder(), idempotentManagerMap, keyGeneratorMap, idempotentManager);
    }

}
